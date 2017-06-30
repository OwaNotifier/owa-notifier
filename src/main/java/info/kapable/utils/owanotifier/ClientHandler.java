package info.kapable.utils.owanotifier;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.IdToken;
import info.kapable.utils.owanotifier.auth.TokenResponse;

public class ClientHandler extends Thread {
	private Socket socket; // The accepted socket from the Webserver
	private String code;
	private String expectedNonce;
	private String idToken;
	private ObservableImpl obs;
	private OwaNotifier on;
	
	public void subscribe(Observer o) {
		 obs  = new ObservableImpl();
		 obs.addObserver(o);
	}
	// Start the thread in the constructor
	public ClientHandler(Socket s, String nonce, OwaNotifier on) {
		socket = s;
		expectedNonce = nonce;
		this.on = on;
		this.subscribe(on);
		start();
	}

	// Read the HTTP request, respond, and close the connection
	public void run() {
		try {
			// Open connections to the socket
			BufferedReader in;
			BufferedWriter out;
			int contentLength = 0;
            final String contentHeader = "Content-Length: ";
            
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			for (String line = in.readLine(); line != null; line = in
					.readLine()) {
				// System.out.println(line);
				if (line.startsWith(contentHeader)) {
					 contentLength = Integer.parseInt(line.substring(contentHeader.length()));
					 break;
	            }
			}

		    StringBuilder dataBuilder = new StringBuilder();
		    int c = 0;
		    for (int i = 0; i < contentLength; i++) {
		    	c = in.read();
		    	dataBuilder.append((char) c);
		    }
		    System.out.println(dataBuilder.toString());
		    String[] data = dataBuilder.toString().split("&");
		    for(int i = 0; i < data.length; i++) {
		    	String[] array = data[i].split("=");
		    	System.out.println(array[0] + " = " + array[1]);
		    	if(array[0].contains("code")) {
		    		this.code = array[1];
		    	}
		    	if(array[0].contains("id_token")) {
		    		this.idToken = array[1];
		    	}
		    }
	        
		    out.write("HTTP/1.1 200 OK\r\n");
		    out.write("Content-Type: text/html\r\n");
		    out.write("\r\n");
		    out.write("<html><body><script>function close_window() { window.close(); } close_window();</script><a href='javascript:close_window();'>Fermer cette fenêtre</a></body><html>");
		 // do not in.close();
		    out.flush();
		    out.close();
	        System.out.println("OK");
	        IdToken idTokenObj = IdToken.parseEncodedToken(idToken, expectedNonce.toString());
	        if (idTokenObj != null) {
	          OwaNotifier.tokenResponse = AuthHelper.getTokenFromAuthCode(code, idTokenObj.getTenantId());
	          obs.update();
	          obs.notifyObservers();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
