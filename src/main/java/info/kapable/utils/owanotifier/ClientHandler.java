package info.kapable.utils.owanotifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.IdToken;
import info.kapable.utils.owanotifier.auth.TokenResponse;

public class ClientHandler extends Thread {
	private Socket socket; // The accepted socket from the Webserver
	private String code;
	private String expectedNonce;
	private String idToken;
	public TokenResponse tokenResponse = null;
	public IdToken idTokenObj;

	// Start the thread in the constructor
	public ClientHandler(Socket s, String nonce) {
		socket = s;
		expectedNonce = nonce;
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

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				OwaNotifier.log(line);
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
			if (contentLength >= 0) {
				OwaNotifier.log(dataBuilder.toString());
				String[] data = dataBuilder.toString().split("&");
				for (int i = 0; i < data.length; i++) {
					String[] array = data[i].split("=");
					System.out.println(array[0] + " = " + array[1]);
					if (array[0].contains("code")) {
						this.code = array[1];
					}
					if (array[0].contains("id_token")) {
						this.idToken = array[1];
					}
				}
			}
			out.write("HTTP/1.1 200 OK\r\n");
			out.write("Content-Type: text/html\r\n");
			out.write("\r\n");
			String closeWindow = OwaNotifier.props.getProperty("closeWindow");
			if(closeWindow == null) {
				closeWindow = "false";
			}
			if(closeWindow.contentEquals("false")) {
				out.write("<html><body><meta http-equiv='refresh' content='0; url=https://outlook.office.com/owa/' /></body></html>");
			} else {
				out.write("<html><body><script>function close_window() { window.close(); } close_window();</script><a href='javascript:close_window();'>Fermer cette fen&ecirc;tre</a></body></html>");
			}
			// do not in.close();
			out.flush();
			out.close();
			this.idTokenObj = IdToken.parseEncodedToken(idToken, expectedNonce.toString());
			if (idTokenObj != null) {
				this.tokenResponse = AuthHelper.getTokenFromAuthCode(code, idTokenObj.getTenantId());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
