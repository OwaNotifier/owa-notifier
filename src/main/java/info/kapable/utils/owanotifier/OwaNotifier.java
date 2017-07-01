package info.kapable.utils.owanotifier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.TokenResponse;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.service.OutlookService;
import info.kapable.utils.owanotifier.service.OutlookServiceBuilder;
import info.kapable.utils.owanotifier.service.OutlookUser;

public class OwaNotifier extends Observable {
	public static boolean testMode = false;
	public TokenResponse tokenResponse;
	public DesktopProxy desktop;
	private String email;
	
	/**
	 * Main function swith from static domain to object
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		OwaNotifier on = new OwaNotifier();
		on.boot();
	}
	
	/**
	 * Boot application
	 */
	private void boot() {
		OwaNotifier.log("--- Owa-Notifier ---");
		this.desktop = new DesktopProxy();
		this.addObserver(this.desktop);
		this.login();
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
		// Redirect user to authentification webpage
		String loginUrl = AuthHelper.getLoginUrl(state, nonce);
		System.out.println(" * loginUrl: " + loginUrl);
		try {
			this.desktop.browse(loginUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			OwaNotifier.exit(3);
		}
		
		// Start a webserver to handle return of authenficatoion
		try {
			ServerSocket serverSocket = new ServerSocket(8080); // Start, listen on port 8080
			Socket s = serverSocket.accept(); // Wait for a client to connect
			ClientHandler c = new ClientHandler(s, nonce.toString()); // Handle the client in a separate thread
			c.join();
			s.close();
			serverSocket.close();
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

	public void infiniteLoop() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		int lastUnreadCount = 0;
		String folder = "inbox";
		JacksonConverter c = new JacksonConverter(new ObjectMapper());
		OutlookService outlookService = OutlookServiceBuilder.getOutlookService(this.tokenResponse.getAccessToken(), email);
		OutlookUser user = (OutlookUser) c.fromBody(outlookService.getCurrentUser().getBody(), OutlookUser.class);
		this.email = user.getMail();
		System.out.println(email);
		
		while (true) {
			Thread.sleep(5000);
		
			// Retrieve messages from the inbox
			Folder inbox = (Folder) c.fromBody(outlookService.getFolder(folder).getBody(), Folder.class);
			if (inbox.getUnreadItemCount() > 0 && lastUnreadCount != inbox.getUnreadItemCount()) {
				this.setChanged();
				this.notifyObservers(inbox);
			}
			lastUnreadCount = inbox.getUnreadItemCount();
		}
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
}