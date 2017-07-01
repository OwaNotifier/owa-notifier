package info.kapable.utils.owanotifier;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import org.junit.Test;

public class ClientHandlerTest {

	@Test
	public void test() {
		final String payloadQuery = "POST /authorize.html";
		Thread client = new Thread() {
			public void run() {
		         try {
					Socket client = new Socket("localhost", 8080);
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
							client.getOutputStream()));
					BufferedReader in = new BufferedReader(new InputStreamReader(
							client.getInputStream()));
					out.write(payloadQuery);
					out.write("\r\n");
					out.close();
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		try {
			final ServerSocket serverSocket = new ServerSocket(8080);
			client.start();
			Socket s = serverSocket.accept();
			UUID nonce = UUID.randomUUID();
			ClientHandler c = new ClientHandler(s, nonce.toString());
			serverSocket.close();
			assertTrue(c.tokenResponse == null);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
