package info.kapable.utils.owanotifier.auth;

import info.kapable.utils.owanotifier.OwaNotifier;
import info.kapable.utils.owanotifier.RestfullAcessProxy;
import info.kapable.utils.owanotifier.teststubs.StubTokenService;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.junit.Test;

import junit.framework.TestCase;

public class AuthHelperTest extends TestCase {
	protected void setUp() throws Exception {
		OwaNotifier.testMode = true; // don't exit
		RestfullAcessProxy.stubMode = true; // use stub to make test
		StubTokenService.getStubTokenService().body = "{\"token_type\":\"Bearer\",\"scope\":\"Mail.Read User.Read\",\"expires_in\":3599,\"ext_expires_in\":0,\"access_token\":\"eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFBOWtUa2xoVnk3U0pUR0F6Ui1wMUJjaHpULTVBeDNYZWFRejJmX3F3OVNjZTlPUktLNHR3Q2hTaHRVbENhdG5GaGNRc0I4MmlERWtpMUF0dExxalJmU1l1QjFpdDNwYVBtc0lZV3QzOFZwdXlBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIiwia2lkIjoiVldWSWMxV0QxVGtzYmIzMDFzYXNNNWtPcTVRIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8xZWVhOWNiZS0yNDNhLTQwYjktYmEyMC1kYzI4ZGIzOTMzYmEvIiwiaWF0IjoxNTAzMzI3OTU2LCJuYmYiOjE1MDMzMjc5NTYsImV4cCI6MTUwMzMzMTg1NiwiYWNyIjoiMSIsImFpbyI6IlkyRmdZQWd2NG42ZnNYeGkzSmJ3cTU2ckdHZXd4cXpUY3c2dnRPSHJLTlc1SGhVUjlSOEEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Im93YS1ub3RpZmllciIsImFwcGlkIjoiZWM3YWQ5YmQtNmNlYi00Y2Q0LThkNjQtYTFiNDgwZmI0Y2I1IiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJHT1VMSU4iLCJnaXZlbl9uYW1lIjoiTWF0aGlldSIsImlwYWRkciI6IjgzLjIwNi4xOTYuNjciLCJuYW1lIjoiTWF0aGlldSBHT1VMSU4iLCJvaWQiOiI5MjU5OGIzOS0zMGMyLTRmNTktYjQ2ZS01YWYwZDQyY2UzZjgiLCJwbGF0ZiI6IjMiLCJwdWlkIjoiMTAwMzNGRkZBMkQzNTMzNSIsInNjcCI6Ik1haWwuUmVhZCBVc2VyLlJlYWQiLCJzdWIiOiJwckFmM3FqVDVPVVFWMllwejNadlJKMDBGdUhOUDRNWVpGNElwZEI3WWhvIiwidGlkIjoiMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhIiwidW5pcXVlX25hbWUiOiJtYXRnb3VAZ291bGluLm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6Im1hdGdvdUBnb3VsaW4ub25taWNyb3NvZnQuY29tIiwidXRpIjoiSjRGYVlSTklDME9hemxJXzE4WUVBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.oKSCn7Q6MegjOjmobnTmNx3PpknCBZ7o1bHN0bz_vYVigfP0xEp47O6ZxdH5Nux9zNJnbKlinpEO3e3ZEm2H_32C5fRCZjZs9g-aqhlpTsWiAszcUSjNwmTO-2hjNlFzgSJR5_Co49FXfG9Yhl9tg-dU8E8YUu91TrUE9Wx5_XDCKGAiTof2VpiTUcaHrrUZwO0zzRUd245nBu73NHLNlNSIkT0MNP0WT3iJk-9d2JzOeDiA-R1qCJyfA805utQojyzP8z3G5PVuZ8cTwvF92SHr4oMMy4d2mGNadYJWz6ZY-TaSaXVAHQkBWlgA4aC--GiPaAuNczEteWEi2qgGcw\",\"refresh_token\":\"OAQABAAAAAAA9kTklhVy7SJTGAzR-p1Bc_UR6mS2z-Pga7twEw_1rJ-gbL3rDwrFxaJGe25exKlpq4NEVJJiFsw7oGse-OjSRTGLNB6BXn6Ns3qpxPASyn5Scv0t0L4joc9GX9og2YvXnnUDK6dXf878cchWwYuRP5z5RO45TScUuf0UGeiRbyP-OujNTFXOpcWcqdgWX1TcFKsSkfcb_ksFG6OF3qZXlCUtoZGbhhEZZDmXYpxgAuwfpaC9QOQoxhqb5wQZY8x1ud-zfQ0tZFOo5xRcB_EzlsWEsYbCFsn8O2Cn7HvYataHYbfI_qlL4WoSkXcY17ZkY0xSRH6VsQUOohL4eCEN7IJ1Znu2DrISGf5inVQIYRJaTfr4N7nbgL7Pg-pUdMi6IObUKCDQXrJJoyl9nK2uTbikNHoCf2uS_BHM0rK2aUl4RooEPqkD5MaUL4Bhv4lyYA5dtmU1oITUKRjvfw-HY_zf0VspEI5odkIQU4xeZH3CCx2NY0KoGpQlpJAGUQ8xfxKD_cplNS6-GX4yMFjlQKCXKmTlXW4h8aju2M48ghdL-VHLHWjK_WOYg-BA6SFuQGIC255lY9xWKTi5rREwHkyTNNkS98fywcu3JF0boxwWwxx29COU2HXQQvflGqiOS4Uu7pqHi50zwxVRx8j5itmHoR_h1SJMEDMDR22DD1dDlZro-ZVzARB-z2VDv8TpG4SxvYtWi_SunXDmLiompq7lSRploOxp7Cv5p3fZ7U3-b3E5Z6oF-ORrcBTeXrDt5097MYVBrghcIswRUN7lbu0wftYG1HApNpbDWmzsUtYCA2GI5cjjggfmFqIhB7LCroJ7iiZjZDZruEy77s6c0bpQ6vzp4L0HsrDfiQ_R4ZyAA\",\"id_token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USJ9.eyJhdWQiOiJlYzdhZDliZC02Y2ViLTRjZDQtOGQ2NC1hMWI0ODBmYjRjYjUiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vMWVlYTljYmUtMjQzYS00MGI5LWJhMjAtZGMyOGRiMzkzM2JhL3YyLjAiLCJpYXQiOjE1MDMzMjc5NTYsIm5iZiI6MTUwMzMyNzk1NiwiZXhwIjoxNTAzMzMxODU2LCJhaW8iOiJBVFFBeS84RUFBQUFoaERSbnRZUVFZRDFwM0toWW52MXRlQ0F3Y1RvU01zck42K2tBTG9zQVFvM2txWWxGMGxmVE9ZMC8ydFV0ODZsIiwibmFtZSI6Ik1hdGhpZXUgR09VTElOIiwibm9uY2UiOiI3ZDIxNDQwNC0wNmY2LTQ5YmUtOWUyOC1jN2JjN2IwODNlZDYiLCJvaWQiOiI5MjU5OGIzOS0zMGMyLTRmNTktYjQ2ZS01YWYwZDQyY2UzZjgiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtYXRnb3VAZ291bGluLm9ubWljcm9zb2Z0LmNvbSIsInN1YiI6IjNtY3V3QWxoOFR2b3NYLXRtbDA1UDN6c2VlTlV2YUlRMG5ReHcycGJVTGciLCJ0aWQiOiIxZWVhOWNiZS0yNDNhLTQwYjktYmEyMC1kYzI4ZGIzOTMzYmEiLCJ2ZXIiOiIyLjAifQ.E16HIXAih8wSRkVVVzkU70kcf6--FMR1BM8vEOO0Mt3B-vDYMEaGo1b2T2mjX9fBcUFwBYRDDSMw4Ze9FUYtG4tBVAKbDyKjrgPtsJWQyG4baQturUWctZtqI_EwtMxaAxuYW77uXZtkXImj_KDq5DLP_VLt55T-Fc9K0N0Rrjwg1FTfJgekAeml7uf3CKFrY8wv6bwfddivpzH-Rd9dbTBMuOqIS38e4XPZ_uMt27Zo9PmDYgdKJI2_1AVEhppWShs8rhId1x_b8t99_llNv4MEpGhSm8HT2XanOjV3t0gN0ybU6N95n6FU0zv2-_FQFSrsankP6ktdIMtqB2boTg\"}";
	}
	
	@Test
	public void testProperties() {
		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();
		String url = "";
		try {
			url = AuthHelper.getLoginUrl(state, nonce, 8080);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(url.contains("state=" + state));
		assertTrue(url.contains("nonce=" + nonce));
		assertTrue(url.contains(":8080"));
	}
	
	@Test
	public void testGetToken() {
		 TokenResponse t = AuthHelper.getTokenFromAuthCode("TestCase", "TestCase");
		 assertTrue(t.getTokenType().contentEquals("Bearer"));
		 Calendar now = Calendar.getInstance();
		 now.add(Calendar.SECOND, 3500);
		 assertTrue(t.getExpirationTime().after(now.getTime()));
		 
		 t = AuthHelper.getTokenFromRefresh(t, "TestCase");
		 assertTrue(t.getTokenType().contentEquals("Bearer"));
		 assertTrue(t.getExpirationTime().after(now.getTime()));	 
	}
	
	@Test
	public void testGetTokenWithException() {
		StubTokenService.getStubTokenService().retrofitError=true;
		TokenResponse t = AuthHelper.getTokenFromAuthCode("TestCase", "TestCase");
		assertTrue(t.getError().length() > 0);
		
		t = AuthHelper.getTokenFromRefresh(t, "TestCase");
		assertTrue(t.getError().length() > 0);
		StubTokenService.getStubTokenService().retrofitError=false;
	}
}
