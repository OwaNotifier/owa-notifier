package info.kapable.utils.owanotifier;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import org.junit.Test;

public class ClientHandlerTest {

	@Test
	public void test() {
		OwaNotifier.testMode = true;
		final String payloadQuery = "POST /authorize.html HTTP/1.1" 
					+ "\r\n" + "Host: localhost:8080\r\n"
					+ "\r\n" + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:52.0) Gecko/20100101 Firefox/52.0"
					+ "\r\n" + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
					+ "\r\n" + "Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3"
					+ "\r\n" + "Accept-Encoding: gzip, deflate"
					+ "\r\n" + "Connection: keep-alive"
					+ "\r\n" + "Upgrade-Insecure-Requests: 1"
					+ "\r\n" + "Content-Type: application/x-www-form-urlencoded"
					+ "\r\n" + "Content-Length: 2036"
					+ "\r\n" + ""
					+ "\r\n" + "code=OAQABAAIAAABnfiG-mA6NTae7CdWW7Qfdy9lfTM4XHQ9bJS13hvMXqAlAX_nRgMSEmGZjkzq5B6yRazGDrXRP2ZyvQPihYZ_MPz7x_Bvf4ABk_t0UhUdhG3IjgQueoevLAfiFDF4Wva218tftJloUq-CbvynAIq4rHz2YT14SypitBR1eH9X7T03_9ht0-M1ihwgXWsm980AAkZWsM6jZpendcSl6KP_FyjQ9XK477lyNhG_MKTyOctSANyjgXqy8Pqn38CJiRYmkVXb-zObCDjsncD_uaNyNTQeq-AVJTNxB7TVMq1CWjCWe40wb7WE2hLTk90U7dtEjYD9nuerGtruhmdfo6cuZIYtN78-0_nSXZXuypAy78yVpdp7Kk2zinLQ2No7H-TYH1qsZYBdxd4nI6wRZIx2T8v0DegHmqiO95aD32cnhFLQjcXAJdxyQmKfphLeqRcmg6Y2FRvk2DNbJs7QGA-LHfORWlveKa-nWZ2lEPQ5StVSfVYfd6xjSFMwv_kgWBltTSvVsaG5r1JZMoct4k8ZubkQRCX8IozBl3iGfxOWG2zikb2TnRJmlSwwdcvJ2aeih5rcuXUMf5mEaEhD8uxDRLaWEo-GP8at2kH9n-Tmmz07Ew_XBBaKZ24UR75W6tXYk8R2VhH8NYBY419sHucZkC9-muDlaRYtZao9hAJYIUfqW-Ou_hdFSv35pC82aXJsgAA&id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjlGWERwYmZNRlQyU3ZRdVhoODQ2WVR3RUlCdyJ9.eyJhdWQiOiJlYzdhZDliZC02Y2ViLTRjZDQtOGQ2NC1hMWI0ODBmYjRjYjUiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhL3YyLjAiLCJpYXQiOjE0OTg5MDkyNDMsIm5iZiI6MTQ5ODkwOTI0MywiZXhwIjoxNDk4OTEzMTQzLCJhaW8iOiJZMlpnWUhnbEtaS240ZlprS1UvZzNlVzY4Y1dzSGpJY1o2L0ZucGpEOG1VR3UwRHRySVVBIiwiY19oYXNoIjoiTUhnTkwwdnp5NzJXejVSbzEybTc1dyIsIm5hbWUiOiJNYXRoaWV1IEdPVUxJTiIsIm5vbmNlIjoiYTg2NmQyNDYtNWUzMy00MjE5LWJiNzgtZDIyYmRiYzIwNWEwIiwib2lkIjoiOTI1OThiMzktMzBjMi00ZjU5LWI0NmUtNWFmMGQ0MmNlM2Y4IiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWF0Z291QGdvdWxpbi5vbm1pY3Jvc29mdC5jb20iLCJzdWIiOiIzbWN1d0FsaDhUdm9zWC10bWwwNVAzenNlZU5VdmFJUTBuUXh3MnBiVUxnIiwidGlkIjoiMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhIiwidXRpIjoiYzBfMUtkRnNCRXE4ZFczaVBIY0RBQSIsInZlciI6IjIuMCJ9.M17WYxec8tGtQG6tAcMv00nWAHSluolHXrBWrtGJUnNwcLIkAo03frSXQbzmfMMc7ty4wsWuSk3hlrSUey-AWIlO72cQRnq66eCBgRYuqBVthbAGBcDbDuKBt00xhp6sI7JupmUv07B3PSuV7Yt7numQvD02khTW_cnJwKpLWboOqUHPinwijjCEcunCNZjM537Eh9P6jMd-qsl6pt10gsQL089YGLSnGLyIQtfNR47tXiJSk1RmtRxcYVZH3t1Bz0txOEyxq1EvtdJdJ3JzEeu9SntpwgIFuImSraid0JtDpNtQNGA2kUUHSFj1UNqMkG7VgRdpCjlP2wiwZN3H5Q&state=a16f5192-5ab6-44e7-b85d-c57af5e830de&session_state=abfc2513-97df-4eb2-84ad-5137a6453a"
					+ "\r\n" ;

		Thread client = new Thread() {
			public void run() {
		         try {
		        	try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Socket client = new Socket("localhost", 8080);
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
							client.getOutputStream()));
					BufferedReader in = new BufferedReader(new InputStreamReader(
							client.getInputStream()));
					out.write(payloadQuery);
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
			// Token is null because expiration time
			assertTrue(c.tokenResponse == null);
			// idToken is not null
			assertTrue(c.idTokenObj != null);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
