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
 */package info.kapable.utils.owanotifier.teststubs;

import info.kapable.utils.owanotifier.auth.TokenService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
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
	public boolean retrofitError = false;
	
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
		if(retrofitError) {
			IOException e = new IOException("JUnit Stub");
			throw RetrofitError.networkError(url, e);
		}
		return new Response(url, status, reason, headers, new TypedString(body));
	}

	@Override
	public Response getAccessTokenFromRefreshToken(String tenantId,
			String clientId, String clientSecret, String grantType,
			String code, String redirectUrl) {

		if(retrofitError) {
			IOException e = new IOException("JUnit Stub");
			throw RetrofitError.networkError(url, e);
		}
		return new Response(url, status, reason, headers, new TypedString(body));
	}

}
