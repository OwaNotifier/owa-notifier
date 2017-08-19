/**
The MIT License (MIT)

Copyright (c) 2017 Mathieu GOULIN

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package info.kapable.utils.owanotifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Properties;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.TokenResponse;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.service.Message;
import info.kapable.utils.owanotifier.service.MessageCollection;
import info.kapable.utils.owanotifier.service.OutlookService;
import info.kapable.utils.owanotifier.service.OutlookServiceBuilder;

/**
 * OwaNotifier main class
 *  - Load config
 *  - start oauth2 client daemon
 *  - main loop to check new mail
 * 
 * @author Mathieu GOULIN <mathieu.goulin@gadz.org>
 */
public class OwaNotifier extends Observable {
	// testMode is true when using this class on jUnit context
	public static boolean testMode = false;
	// props contains application configuration
	public static Properties props;
	
	// A public object to store auth 
	public TokenResponse tokenResponse;
	
	/**
	 * Load config from properties in ressource
	 * @throws IOException
	 */
	private static void loadConfig() throws IOException {
		String authConfigFile = "auth.properties";
		InputStream authConfigStream = AuthHelper.class.getClassLoader().getResourceAsStream(authConfigFile);

		if (authConfigStream != null) {
			props = new Properties();
			props.load(authConfigStream);
		} else {
			throw new FileNotFoundException("Property file '" + authConfigFile + "' not found in the classpath.");
		}
	}
	
	/**
	 * return properties
	 * @return
	 * @throws IOException
	 */
	public static Properties getProps() throws IOException {
		if(OwaNotifier.props == null) {
			loadConfig();
		}
		return OwaNotifier.props;
	}
	
	/**
	 * Update a property
	 * @param key
	 * @param value
	 * @throws IOException 
	 */
	static void setProps(String key, String value) throws IOException {
		loadConfig();
		OwaNotifier.props.put(key, value);
	}

	/**
	 * Log a message to console
	 * @param msg
	 */
	public static void log(String msg) {
		System.out.println(msg);
	}

	/**
	 * Exit application with code
	 * @param rc code to exit application
	 */
	public static void exit(int rc) {
		log("Exit with code : " + rc);
		System.exit(rc);
	}
	/**
	 * Main function swith from static domain to object
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		OwaNotifier on = new OwaNotifier();
		loadConfig();
		on.boot();
	}
	
	/**
	 * Boot application
	 */
	private void boot() {
		OwaNotifier.log("--- Owa-Notifier ---");
		
		// Add different notification observer
		//
		this.addObserver(new DesktopProxy());
		
		// Load token from oauth2 process
		// Display login page
		this.login();
		
		// When login is ok loop
		try {
			this.infiniteLoop();
		} catch (JsonParseException e) {
			e.printStackTrace();
			OwaNotifier.exit(6);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			OwaNotifier.exit(6);
		} catch (IOException e) {
			e.printStackTrace();
			OwaNotifier.exit(6);
		} catch (InterruptedException e) {
			e.printStackTrace();
			OwaNotifier.exit(6);
		}
	}
	
	/**
	 * login operation
	 */
	private void login() {
		// Generate UUID for login
		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();
		
		// Start a webserver to handle return of oauth
		try {
			int listenPort = Integer.parseInt(OwaNotifier.getProps().getProperty("listenPort", "8080"));
			ServerSocket serverSocket = null;
			
			// Search an available port
			while(serverSocket == null) {
				try {
					OwaNotifier.log("update listen port to " + listenPort);
					serverSocket = new ServerSocket(listenPort); // Start, listen on port 8080
				} catch (BindException e){
					listenPort = listenPort +1;
					OwaNotifier.setProps("listenPort", listenPort + "");
				}
			}
			
			// Redirect user to ms authentification webpage
			String loginUrl = AuthHelper.getLoginUrl(state, nonce, listenPort);
			OwaNotifier.log(" * loginUrl: " + loginUrl);
			try {
				DesktopProxy.browse(loginUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				OwaNotifier.exit(3);
			}
			// Wait for a client to connect
			Socket s = serverSocket.accept(); 
			ClientHandler c = new ClientHandler(s, nonce.toString()); // Handle the client in a separate thread
			// Wait return of webserver
			c.join();
			s.close();
			serverSocket.close();
			
			// Save tokenResponse in case of success
			if(c.tokenResponse == null) {
				OwaNotifier.log("No token, error");
				OwaNotifier.exit(5);
			} else {
				this.tokenResponse = c.tokenResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
			OwaNotifier.exit(2);
		}
	}

	/**
	 * Loop until end to get unread mail count
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void infiniteLoop() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		int lastUnreadCount = 0;
		String folder = "inbox";
		JacksonConverter c = new JacksonConverter(new ObjectMapper());
		OutlookService outlookService = OutlookServiceBuilder.getOutlookService(this.tokenResponse.getAccessToken(), null);
		
		while (true) {
			Thread.sleep(5000);
			
			// Retrieve messages from the inbox
			Folder inbox = (Folder) c.fromBody(outlookService.getFolder(folder).getBody(), Folder.class);
			
			if (inbox.getUnreadItemCount() > 0 &&  inbox.getUnreadItemCount() > (lastUnreadCount +1)) {
				this.setChanged();
				this.notifyObservers(new InboxChangeEvent(inbox, InboxChangeEvent.TYPE_MANY_NEW_MSG, inbox.getUnreadItemCount() + " message(s) non lu"));
			}
			if (inbox.getUnreadItemCount() > 0 &&  inbox.getUnreadItemCount() == (lastUnreadCount +1)) {
				this.setChanged();
				MessageCollection m = (MessageCollection) c.fromBody(outlookService.getMessages(inbox.getId(), "receivedDateTime desc", "from,subject,bodyPreview", "isRead eq false", 1).getBody(), MessageCollection.class);
				Message message = (Message) m.getValue().get(0);
				this.notifyObservers(new InboxChangeEvent(inbox, message));
			}
			if (inbox.getUnreadItemCount() > 0 &&  inbox.getUnreadItemCount() < lastUnreadCount) {
				this.setChanged();
				this.notifyObservers(new InboxChangeEvent(inbox, InboxChangeEvent.TYPE_LESS_NEW_MSG, inbox.getUnreadItemCount() + " message(s) non lu"));
			}
			lastUnreadCount = inbox.getUnreadItemCount();
		}
	}
}