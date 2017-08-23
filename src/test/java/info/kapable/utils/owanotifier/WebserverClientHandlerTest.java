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
 */package info.kapable.utils.owanotifier;

import info.kapable.utils.owanotifier.auth.TokenResponse;
import info.kapable.utils.owanotifier.auth.WebserverClientHandler;
import info.kapable.utils.owanotifier.teststubs.StubTokenService;
import info.kapable.utils.owanotifier.utils.HTTPBasicClient;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

public class WebserverClientHandlerTest extends TestCase {
	
	private String payloadQuery = "POST /authorize.html HTTP/1.1" 
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
			+ "\r\n" + "code=OAQABAAIAAAA9kTklhVy7SJTGAzR-p1BcrJSs0Nq7SigqTKtuAN4qkyU-Q9BBoDbF2oyuO2CTwvD_Xuuu81861NABu7wpDf00MxYUZJcQdeJl1xG0wodnax2TDtMm9RYMXuOjpA5GMirgqF4d7v8FVEFyMgmcpOunPhNn8Xj3CXVNkX0aWyMo2UnGmMg1P81EF7CR08Q23SM2miPSDHPuBNhkKxFIKTG4fjudn_j4Nz81ohr0GOWatmafu1_mR4szpNj5Fs6lBHZodyHJuPMp_djalRmG2U2rARri7WS2JD0IkYv1TfUWQRBEvN5VxYMwuOey0MUasKa0KJICp9zbvsAyQNztTyc6SyF0FsxMi3K65z6rtqNGoLHkj9rA61H-Amz4xGqvQXhlD9Jna9njutraMvoHKjd6w0Z1zf3XKXu4bjp82y1zn0ChVdTpChGaGQ4I343B9DN01uLSIn8_hjRb9AcFnF_XyTHN4wlnF8Z-T-BMkQWmqhkny121frZChd8l4h12ylmw-7BwWxMNbxEbNqcgy9Wj7qovZ5ElI49ehxMdORu0y3XIuPUlGs5ET6F2CWG7bFFa6UkLdVeH-07vWSSaCOw5Kalc60ePgbEEMnbCBQC_LoS9reFD72S-XMJesdVhL0NbGYaeMNFic2gsV2bpqeheFo2gFFB2yKAdp9Btdl1XdzlvVEfmdFbtmzQDv1sRwXMgAA&id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USJ9.eyJhdWQiOiJlYzdhZDliZC02Y2ViLTRjZDQtOGQ2NC1hMWI0ODBmYjRjYjUiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhL3YyLjAiLCJpYXQiOjE1MDMzMjc5NTAsIm5iZiI6MTUwMzMyNzk1MCwiZXhwIjoxNTAzMzMxODUwLCJhaW8iOiJBVFFBeS84RUFBQUFvNzZnaWFxZUZKSzljU3FKVjVldmlQZFlseE9WMzNxOVVOdkI5RHRSdlFFZEVvOTFWYVRsY3htMlNHNWhMUmZnIiwiY19oYXNoIjoiNTA3ekQ3b0xPaTdrcVo5ZHF4MUxxUSIsIm5hbWUiOiJNYXRoaWV1IEdPVUxJTiIsIm5vbmNlIjoiN2QyMTQ0MDQtMDZmNi00OWJlLTllMjgtYzdiYzdiMDgzZWQ2Iiwib2lkIjoiOTI1OThiMzktMzBjMi00ZjU5LWI0NmUtNWFmMGQ0MmNlM2Y4IiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWF0Z291QGdvdWxpbi5vbm1pY3Jvc29mdC5jb20iLCJzdWIiOiIzbWN1d0FsaDhUdm9zWC10bWwwNVAzenNlZU5VdmFJUTBuUXh3MnBiVUxnIiwidGlkIjoiMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhIiwidmVyIjoiMi4wIn0.MUyDj__z2-7nGyitiM_yNzbt1WdMN_upTHC-GvpGTjH1fTe3kmdldDic9QJxa8B0uLzoorRu4JcE_T4UhLOP-I7c0qL1ltB8XN77v_p-atBM3F78korgyL2hbl89mVkdbV-puITRX0SLimXbxal_xmZ-01EogWT6jhiOQEqe0_v0MnzGZkI-XoSsoJBevzm3nz1D0ZW_AC_m4hG7VFoWlYAoHjsXm36pEm7NVI_PqTWN0YBzHpxIIq0l2BJraXKcNFWCKxNjjqkmufrd8eFNL5t65Us70ntjoTEzh0UfX5yVacfXhv5KrR-3rol1BaWoInXHR55odLBdB4kP5b3FBg&state=071c4912-d7d8-476a-a8dd-0a9401b80fc5&session_state=8e34c82b-9ba7-4841-8293-a6353ca72f"
			+ "\r\n" ;
	
	protected void setUp() throws Exception {
		OwaNotifier.testMode = true; // don't exit
		RestfullAcessProxy.stubMode = true; // use stub to make test
		StubTokenService.getStubTokenService().body = "{\"token_type\":\"Bearer\",\"scope\":\"Mail.Read User.Read\",\"expires_in\":3599,\"ext_expires_in\":0,\"access_token\":\"eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFBOWtUa2xoVnk3U0pUR0F6Ui1wMUJjaHpULTVBeDNYZWFRejJmX3F3OVNjZTlPUktLNHR3Q2hTaHRVbENhdG5GaGNRc0I4MmlERWtpMUF0dExxalJmU1l1QjFpdDNwYVBtc0lZV3QzOFZwdXlBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIiwia2lkIjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8xZWVhOWNiZS0yNDNhLTQwYjktYmEyMC1kYzI4ZGIzOTMzYmEvIiwiaWF0IjoxNTAzMzI3OTU2LCJuYmYiOjE1MDMzMjc5NTYsImV4cCI6MTUwMzMzMTg1NiwiYWNyIjoiMSIsImFpbyI6IlkyRmdZQWd2NG42ZnNYeGkzSmJ3cTU2ckdHZXd4cXpUY3c2dnRPSHJLTlc1SGhVUjlSOEEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Im93YS1ub3RpZmllciIsImFwcGlkIjoiZWM3YWQ5YmQtNmNlYi00Y2Q0LThkNjQtYTFiNDgwZmI0Y2I1IiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJHT1VMSU4iLCJnaXZlbl9uYW1lIjoiTWF0aGlldSIsImlwYWRkciI6IjgzLjIwNi4xOTYuNjciLCJuYW1lIjoiTWF0aGlldSBHT1VMSU4iLCJvaWQiOiI5MjU5OGIzOS0zMGMyLTRmNTktYjQ2ZS01YWYwZDQyY2UzZjgiLCJwbGF0ZiI6IjMiLCJwdWlkIjoiMTAwMzNGRkZBMkQzNTMzNSIsInNjcCI6Ik1haWwuUmVhZCBVc2VyLlJlYWQiLCJzdWIiOiJwckFmM3FqVDVPVVFWMllwejNadlJKMDBGdUhOUDRNWVpGNElwZEI3WWhvIiwidGlkIjoiMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhIiwidW5pcXVlX25hbWUiOiJtYXRnb3VAZ291bGluLm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6Im1hdGdvdUBnb3VsaW4ub25taWNyb3NvZnQuY29tIiwidXRpIjoiSjRGYVlSTklDME9hemxJXzE4WUVBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.oKSCn7Q6MegjOjmobnTmNx3PpknCBZ7o1bHN0bz_vYVigfP0xEp47O6ZxdH5Nux9zNJnbKlinpEO3e3ZEm2H_32C5fRCZjZs9g-aqhlpTsWiAszcUSjNwmTO-2hjNlFzgSJR5_Co49FXfG9Yhl9tg-dU8E8YUu91TrUE9Wx5_XDCKGAiTof2VpiTUcaHrrUZwO0zzRUd245nBu73NHLNlNSIkT0MNP0WT3iJk-9d2JzOeDiA-R1qCJyfA805utQojyzP8z3G5PVuZ8cTwvF92SHr4oMMy4d2mGNadYJWz6ZY-TaSaXVAHQkBWlgA4aC--GiPaAuNczEteWEi2qgGcw\",\"refresh_token\":\"OAQABAAAAAAA9kTklhVy7SJTGAzR-p1Bc_UR6mS2z-Pga7twEw_1rJ-gbL3rDwrFxaJGe25exKlpq4NEVJJiFsw7oGse-OjSRTGLNB6BXn6Ns3qpxPASyn5Scv0t0L4joc9GX9og2YvXnnUDK6dXf878cchWwYuRP5z5RO45TScUuf0UGeiRbyP-OujNTFXOpcWcqdgWX1TcFKsSkfcb_ksFG6OF3qZXlCUtoZGbhhEZZDmXYpxgAuwfpaC9QOQoxhqb5wQZY8x1ud-zfQ0tZFOo5xRcB_EzlsWEsYbCFsn8O2Cn7HvYataHYbfI_qlL4WoSkXcY17ZkY0xSRH6VsQUOohL4eCEN7IJ1Znu2DrISGf5inVQIYRJaTfr4N7nbgL7Pg-pUdMi6IObUKCDQXrJJoyl9nK2uTbikNHoCf2uS_BHM0rK2aUl4RooEPqkD5MaUL4Bhv4lyYA5dtmU1oITUKRjvfw-HY_zf0VspEI5odkIQU4xeZH3CCx2NY0KoGpQlpJAGUQ8xfxKD_cplNS6-GX4yMFjlQKCXKmTlXW4h8aju2M48ghdL-VHLHWjK_WOYg-BA6SFuQGIC255lY9xWKTi5rREwHkyTNNkS98fywcu3JF0boxwWwxx29COU2HXQQvflGqiOS4Uu7pqHi50zwxVRx8j5itmHoR_h1SJMEDMDR22DD1dDlZro-ZVzARB-z2VDv8TpG4SxvYtWi_SunXDmLiompq7lSRploOxp7Cv5p3fZ7U3-b3E5Z6oF-ORrcBTeXrDt5097MYVBrghcIswRUN7lbu0wftYG1HApNpbDWmzsUtYCA2GI5cjjggfmFqIhB7LCroJ7iiZjZDZruEy77s6c0bpQ6vzp4L0HsrDfiQ_R4ZyAA\",\"id_token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USJ9.eyJhdWQiOiJlYzdhZDliZC02Y2ViLTRjZDQtOGQ2NC1hMWI0ODBmYjRjYjUiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhL3YyLjAiLCJpYXQiOjE1MDMzMjc5NTYsIm5iZiI6MTUwMzMyNzk1NiwiZXhwIjoxNTAzMzMxODU2LCJhaW8iOiJBVFFBeS84RUFBQUFoaERSbnRZUVFZRDFwM0toWW52MXRlQ0F3Y1RvU01zck42K2tBTG9zQVFvM2txWWxGMGxmVE9ZMC8ydFV0ODZsIiwibmFtZSI6Ik1hdGhpZXUgR09VTElOIiwibm9uY2UiOiI3ZDIxNDQwNC0wNmY2LTQ5YmUtOWUyOC1jN2JjN2IwODNlZDYiLCJvaWQiOiI5MjU5OGIzOS0zMGMyLTRmNTktYjQ2ZS01YWYwZDQyY2UzZjgiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtYXRnb3VAZ291bGluLm9ubWljcm9zb2Z0LmNvbSIsInN1YiI6IjNtY3V3QWxoOFR2b3NYLXRtbDA1UDN6c2VlTlV2YUlRMG5ReHcycGJVTGciLCJ0aWQiOiIxZWVhOWNiZS0yNDNhLTQwYjktYmEyMC1kYzI4ZGIzOTMzYmEiLCJ2ZXIiOiIyLjAifQ.E16HIXAih8wSRkVVVzkU70kcf6--FMR1BM8vEOO0Mt3B-vDYMEaGo1b2T2mjX9fBcUFwBYRDDSMw4Ze9FUYtG4tBVAKbDyKjrgPtsJWQyG4baQturUWctZtqI_EwtMxaAxuYW77uXZtkXImj_KDq5DLP_VLt55T-Fc9K0N0Rrjwg1FTfJgekAeml7uf3CKFrY8wv6bwfddivpzH-Rd9dbTBMuOqIS38e4XPZ_uMt27Zo9PmDYgdKJI2_1AVEhppWShs8rhId1x_b8t99_llNv4MEpGhSm8HT2XanOjV3t0gN0ybU6N95n6FU0zv2-_FQFSrsankP6ktdIMtqB2boTg\"}";
	}
	
	@Test
	public void testWithCloseWindow() {
		try {
			OwaNotifier.setProps("closeWindow", "true");
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		try {
			OwaNotifier.setProps("listenPort", "8080");
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		doGet(8080, " close_window();");
	}
	
	@Test
	public void testWithCloseWindowNull() {
		try {
			OwaNotifier.setProps("closeWindow", null);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		try {
			OwaNotifier.setProps("listenPort", "8081");
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		doGet(8081, "https://outlook.office.com/owa/");
	}
	
	@Test
	public void testWithoutCloseWindow() {
		try {
			OwaNotifier.setProps("closeWindow", "false");
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		try {
			OwaNotifier.setProps("listenPort", "8081");
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		TokenResponse t = doGet(8081, "https://outlook.office.com/owa/");
		assertTrue(t.getError() == null);
		assertTrue(t.getAccessToken().length() > 0);
	}
	
	private TokenResponse doGet(int listenPort, String verifString) {
		try {
			ServerSocket serverSocket = null;
			// Search an available port
			while(serverSocket == null) {
				try {
					serverSocket = new ServerSocket(listenPort); // Start, listen on port 8080
				} catch (BindException e){
					listenPort = listenPort +1;
					OwaNotifier.setProps("listenPort", listenPort + "");
				}
			}
			HTTPBasicClient client = new HTTPBasicClient(payloadQuery, "localhost", listenPort);
			client.start();
			Socket s = serverSocket.accept();
			UUID nonce = UUID.randomUUID();
			WebserverClientHandler c = new WebserverClientHandler(s, nonce.toString());
			try {
				c.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Token is null because expiration time
			assertTrue(c.tokenResponse != null);
			// idToken is not null
			assertTrue(c.idTokenObj != null);
			s.close();
			serverSocket.close();
			try {
				client.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertTrue(client.output.contains(verifString));
			return c.tokenResponse;
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		return null;
	}

}
