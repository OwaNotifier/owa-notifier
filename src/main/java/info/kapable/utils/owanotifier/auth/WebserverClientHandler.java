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
package info.kapable.utils.owanotifier.auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.kapable.utils.owanotifier.OwaNotifier;

public class WebserverClientHandler extends Thread {
	private Socket socket; // The accepted socket from the Webserver
	private String code;
	private String expectedNonce;
	private String idToken;
	public TokenResponse tokenResponse = null;
	public IdToken idTokenObj;

	// The logger
    private static Logger logger = LoggerFactory.getLogger(WebserverClientHandler.class);
    
	// Start the thread in the constructor
	public WebserverClientHandler(Socket s, String nonce) {
		socket = s;
		expectedNonce = nonce;
		start();
	}

	/**
	 * Return string of file content
	 * @param fileName
	 * 		The name of file to read in ressources
	 * @return
	 * @throws IOException
	 */
	private String readFileAsString(String fileName) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + fileName)));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
	
	// Read the HTTP request, respond, and close the connection
	public void run() {
		try {
			// Open connections to the socket
			BufferedReader in;
			BufferedWriter out;
			int contentLength = 0;
			final String contentHeader = "Content-Length: ";
			StringBuilder dataBuilder = new StringBuilder();

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				logger.debug(line);
				if (line.startsWith("GET ")) {
					if(line.contains("?")) {
						String params = line.substring(line.indexOf("?")+1, line.indexOf(" ", line.indexOf("?")+1));
						dataBuilder.append(params);
					}
				}
				if (line.startsWith(contentHeader)) {
					contentLength = Integer.parseInt(line.substring(contentHeader.length()));
					break;
				}
			    if (line.length() == 0) {
			        break;
			    }
			}

			int c = 0;
			for (int i = 0; i < contentLength; i++) {
				c = in.read();
				dataBuilder.append((char) c);
			}
			if (dataBuilder.toString().length() > 0) {
				logger.debug(dataBuilder.toString());
				String[] data = dataBuilder.toString().split("&");
				for (int i = 0; i < data.length; i++) {
					String[] array = data[i].split("=");
					logger.debug(array[0] + " = " + array[1]);
					if (array[0].contains("code")) {
						this.code = array[1];
					}
					if (array[0].contains("id_token")) {
						this.idToken = array[1];
					}
				}
			} else if(dataBuilder.toString().length() <= 0 ){
				out.write("HTTP/1.1 200 OK\r\n");
				out.write("Content-Type: text/html\r\n");
				out.write("\r\n");
				out.write(readFileAsString("getParamsAndRedirect.html"));
				out.flush();
				out.close();
				
				return;
			}
			out.write("HTTP/1.1 200 OK\r\n");
			out.write("Content-Type: text/html\r\n");
			out.write("\r\n");
			String closeWindow = OwaNotifier.getProps().getProperty("closeWindow");
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
			if(idToken == null) {
				logger.error("No token in return");
				OwaNotifier.exit(5);
			}
			this.idTokenObj = IdToken.parseEncodedToken(idToken, expectedNonce.toString());
			if (idTokenObj != null) {
				this.tokenResponse = AuthHelper.getTokenFromAuthCode(code, idTokenObj.getTenantId());
				if(this.tokenResponse.getError() == null) {
					logger.info("User has a valid token");
				} else {
					logger.error(this.tokenResponse.getError());
					logger.error(this.tokenResponse.getErrorDescription());
					logger.error("User don't have a valid token");
					OwaNotifier.exit(255);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			OwaNotifier.exit(255);
		}
	}
}
