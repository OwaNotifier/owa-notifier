package info.kapable.utils.owanotifier.teststubs;

import info.kapable.utils.owanotifier.auth.TokenService;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

public class StubTokenService implements TokenService {

	public String url = "https://login.microsoftonline.com/1eea9cbe-243a-40b9-ba20-dc28db3933ba/oauth2/v2.0/token";
	public int status = 200;
	public List<Header> headers;
	public String reason = "OK";
	public String body;
	private static StubTokenService stubTokenService;
	
	/**
	 * Singleton
	 * @return
	 * 		The singleton object
	 */
	public static StubTokenService getStubTokenService() {
		if(stubTokenService == null) {
			stubTokenService = new StubTokenService();
		}
		return stubTokenService;
	}
	
	/**
	 * Constructor
	 */
	private StubTokenService() {
		this.headers = new ArrayList<Header>();
	}


	@Override
	public Response getAccessTokenFromAuthCode(String tenantId,
			String clientId, String clientSecret, String grantType,
			String code, String redirectUrl) {
		
		return new Response(url, status, reason, headers, new TypedString(body));
	}

	@Override
	public Response getAccessTokenFromRefreshToken(String tenantId,
			String clientId, String clientSecret, String grantType,
			String code, String redirectUrl) {
		
		return new Response(url, status, reason, headers, new TypedString(body));
	}

}
