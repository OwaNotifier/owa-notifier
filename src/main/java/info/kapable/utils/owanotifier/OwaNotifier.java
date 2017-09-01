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

import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.AuthListner;
import info.kapable.utils.owanotifier.auth.IdToken;
import info.kapable.utils.owanotifier.auth.TokenResponse;
import info.kapable.utils.owanotifier.desktop.DesktopProxy;
import info.kapable.utils.owanotifier.desktop.LogWindowPanel;
import info.kapable.utils.owanotifier.desktop.SwingDesktopProxy;
import info.kapable.utils.owanotifier.desktop.SystemDesktopProxy;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.service.Message;
import info.kapable.utils.owanotifier.service.MessageCollection;
import info.kapable.utils.owanotifier.service.OutlookService;
import info.kapable.utils.owanotifier.service.OutlookServiceBuilder;
import info.kapable.utils.owanotifier.webserver.InternalWebServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * OwaNotifier main class
 *  - Load config
 *  - start oauth2 client daemon
 *  - main loop to check new mail
 * 
 * @author Mathieu GOULIN
 */
public class OwaNotifier extends Observable implements Observer {
	private static final long LOOP_WAIT_TIME = 5000;
	public static final String OWA_URL = "https://outlook.office365.com/owa/";
	// testMode is true when using this class on jUnit context
	public static boolean testMode = false;
	// the return code for exit
	private static int rc;
	
	// props contains application configuration
	public static Properties props;
	
	// A public object to store auth 
	public TokenResponse tokenResponse;
	private IdToken idToken;
	private File lock;
	
	// The logger
    private static Logger logger = LoggerFactory.getLogger(OwaNotifier.class);
	private static OwaNotifier owanotifier;
        
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
	 * 		Return application properties
	 * @throws IOException
	 * 		In case of exception during loading properties
	 */
	public Properties getProps() throws IOException {
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
	public void setProps(String key, String value) throws IOException {
		this.getProps();
		if(value != null)
			OwaNotifier.props.put(key, value);
		else
			OwaNotifier.props.remove(key);
	}

	/**
	 * Exit application with code
	 * @param rc code to exit application
	 */
	public static void exit(int rc) {
		logger.info("Exit with code : " + rc);
		if(!testMode) {
			System.exit(rc);
		} else {
			OwaNotifier.rc = rc;
		}
		
	}
	/**
	 * Main function swith from static domain to object
	 * @param args
	 * 		Arguments passed to application
	 * @throws IOException
	 * 		In case of exception during configuration loading
	 */
	public static void main(String[] args) throws IOException {
		owanotifier = getInstance();
		loadConfig();
		LogWindowPanel.getInstance();
	    // Check if lock exist
		String tmp = System.getProperty("java.io.tmpdir");
		owanotifier.lock = new File(tmp, "owanotifier.lock");
	    if(owanotifier.lock.isFile()) {
	    	long lm = owanotifier.lock.lastModified();
	    	logger.info("Lock modified time : " + lm);
	    	logger.info("System current time : " + System.currentTimeMillis());
	    	// If lock is not update
	    	if((System.currentTimeMillis() - lm) < (LOOP_WAIT_TIME * 2)) {
		    	logger.info("Lock modified time < " + (LOOP_WAIT_TIME * 2) + " => Exit 0");
	    		owanotifier.redirectUserToWebMail();
	    		exit(0);
	    	}
	    }
		owanotifier.boot();
	}
	
	/**
	 * @throws MalformedURLException 
	 * 
	 */
	private void redirectUserToWebMail() throws MalformedURLException {
		DesktopProxy.browse(OwaNotifier.OWA_URL);
	}

	/**
	 * Boot application
	 */
	private void boot() {
		logger.info("--- Owa-Notifier ---");
		
		// Add different notification observer
		//
		this.addObserver(new SystemDesktopProxy());
		this.addObserver(new SwingDesktopProxy());
		
		// Load token from oauth2 process
		// Display login page
		this.login();
	}
	
	/**
	 * login operation
	 */
	private void login() {
		// Generate UUID for login
		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();
		
		try {
			// Start AuthListener
			// Start a web server to handle return of OAuth
			AuthListner listner = new InternalWebServer(nonce);
			
			// Add observer listener to start loop after authentication
			listner.addObserver(this);
			
			// Redirect user to MS authentication web page
			int listenPort = Integer.parseInt(this.getProps().getProperty("listenPort"));
			String loginUrl = AuthHelper.getLoginUrl(state, nonce, listenPort);
			logger.info("Redirect user to loginUrl: " + loginUrl);
			try {
				DesktopProxy.browse(loginUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				OwaNotifier.exit(3);
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
	 * 		In case of bad response fron api
	 * @throws JsonMappingException
	 * 		In case of bad response fron api
	 * @throws IOException
	 * 		In case of exception during api call
	 * @throws InterruptedException
	 * 		In case of interupt
	 */
	public void infiniteLoop() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		int lastUnreadCount = -1;
		String folder = "inbox";
		JacksonConverter c = new JacksonConverter(new ObjectMapper());
		OutlookService outlookService = OutlookServiceBuilder.getOutlookService(this.tokenResponse.getAccessToken(), null);
		
		while (true) {
			Thread.sleep(LOOP_WAIT_TIME);
		    Calendar now = Calendar.getInstance();
		    this.updateLock();
		    
		    // If token is expired refresh token
			if(this.tokenResponse.getExpirationTime().before(now.getTime())) {
				logger.info("Refresh Token");
				this.tokenResponse = AuthHelper.getTokenFromRefresh(this.tokenResponse, this.idToken.getTenantId());
				outlookService = OutlookServiceBuilder.getOutlookService(this.tokenResponse.getAccessToken(), null);				
			}
			// Retrieve messages from the inbox
			Folder inbox = (Folder) c.fromBody(outlookService.getFolder(folder).getBody(), Folder.class);
			logger.info("New Inbox UnreadItemCount : " + inbox.getUnreadItemCount());

			// First loop iteration change notification icon to no mail
			if(lastUnreadCount <= 0) {
				this.setChanged();
				this.notifyObservers(new InboxChangeEvent(inbox, InboxChangeEvent.TYPE_LESS_NEW_MSG));
			}
			
			if (inbox.getUnreadItemCount() > 0 &&  inbox.getUnreadItemCount() > (lastUnreadCount +1)) {
				this.setChanged();
				this.notifyObservers(new InboxChangeEvent(inbox, InboxChangeEvent.TYPE_MANY_NEW_MSG));
			}
			if (inbox.getUnreadItemCount() > 0 &&  inbox.getUnreadItemCount() == (lastUnreadCount +1)) {
				this.setChanged();
				MessageCollection m = (MessageCollection) c.fromBody(outlookService.getMessages(inbox.getId(), "receivedDateTime desc", "from,subject,bodyPreview", "isRead eq false", 1).getBody(), MessageCollection.class);
				Message message = (Message) m.getValue().get(0);
				this.notifyObservers(new InboxChangeEvent(inbox, message));
			}
			if (inbox.getUnreadItemCount() > 0 &&  inbox.getUnreadItemCount() < lastUnreadCount) {
				this.setChanged();
				this.notifyObservers(new InboxChangeEvent(inbox, InboxChangeEvent.TYPE_LESS_NEW_MSG));
			}
			lastUnreadCount = inbox.getUnreadItemCount();
			System.gc();
			Runtime runtime = Runtime.getRuntime();

			NumberFormat format = NumberFormat.getInstance();

			long maxMemory = runtime.maxMemory();
			long allocatedMemory = runtime.totalMemory();
			long freeMemory = runtime.freeMemory();
			logger.debug("==================================================");
			logger.debug("free memory: " + format.format(freeMemory / 1024));
			logger.debug("allocated memory: " + format.format(allocatedMemory / 1024));
			logger.debug("max memory: " + format.format(maxMemory / 1024));
			logger.debug("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
		}
	}

	/**
	 * Update lock file
	 * @throws IOException 
	 */
	private void updateLock() throws IOException {
		logger.debug("Update lock file");
		FileWriter writer = new FileWriter(this.lock);
	    writer.write(System.currentTimeMillis() + "");
	    writer.close();
	}

	/**
	 * @return
	 * 	The return code of application in case of exit
	 */
	public static int getRc() {
		return rc;
	}

	public static OwaNotifier getInstance() {
		if(owanotifier == null) {
			owanotifier = new OwaNotifier();
		}
		return owanotifier;
	}

	@Override
	public void update(Observable o, Object arg) {
		InternalWebServer authListner = (InternalWebServer) o;
		if(authListner.tokenResponse == null) {
			logger.error("No token, error");
			OwaNotifier.exit(5);
		} else {
			this.tokenResponse = authListner.tokenResponse;
			this.idToken = authListner.idTokenObj;
		}
		try {
			this.infiniteLoop();
		} catch (JsonParseException e) {
			logger.error("JsonParseException durring infiniteLoop()",e);
			e.printStackTrace();
			OwaNotifier.exit(6);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException durring infiniteLoop()",e);
			e.printStackTrace();
			OwaNotifier.exit(6);
		} catch (IOException e) {
			logger.error("IOException durring infiniteLoop()",e);
			e.printStackTrace();
			OwaNotifier.exit(6);
		} catch (InterruptedException e) {
			logger.error("InterruptedException durring infiniteLoop()",e);
			e.printStackTrace();
			OwaNotifier.exit(6);
		}
		
	}
}