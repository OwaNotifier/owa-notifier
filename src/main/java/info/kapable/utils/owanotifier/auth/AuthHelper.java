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

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.owanotifier.JacksonConverter;
import info.kapable.utils.owanotifier.OwaNotifier;
import info.kapable.utils.owanotifier.RestfullAcessProxy;

/**
 * Helper class to login on Microsoft using oauth2
 * 
 * @author Mathieu GOULIN
 */
public class AuthHelper {
	// link to microsoft login
	private static final String authority = "https://login.microsoftonline.com";
	// link to oauth
	private static final String authorizeUrl = authority
			+ "/common/oauth2/v2.0/authorize";

	// Scope to request 
	private static String[] scopes = { "openid", "offline_access", "profile",
			"User.Read", "Mail.Read" };

	// Param fetch from configuration
	// AppId and password is need
	private static String appId = null;
	private static String appPassword = null;
	private static String redirectUrl = null;

	// The logger
    private static Logger logger = LoggerFactory.getLogger(AuthHelper.class);
    
    /**
     * Singleton to fetch appId from configuration
     * @return
     * 		The applicationID to use
     */
	private static String getAppId() {
		if (appId == null) {
			try {
				appId = OwaNotifier.getInstance().getProps().getProperty("appId");
			} catch (Exception e) {
				return null;
			}
		}
		return appId;
	}

	/**
	 * Singleton to fetch appPassword from configuration
	 * @return
	 * 		The application password to use
	 */
	private static String getAppPassword() {
		if (appPassword == null) {
			try {
				appPassword = OwaNotifier.getInstance().getProps().getProperty("appPassword");
			} catch (Exception e) {
				return null;
			}
		}
		return appPassword;
	}

	/**
	 * Return the redirect url to pass on microsoft auth
	 * @return
	 * 		An url to microsoft auth
	 * @throws NumberFormatException
	 * 		In case of listenPort is not integer in properties
	 * @throws IOException
	 * 		In case of exception during properties reading
	 */
	private static String getRedirectUrl() throws NumberFormatException,
			IOException {
		int listenPort = Integer.parseInt(OwaNotifier.getInstance().getProps().getProperty(
				"listenPort"));
		if (redirectUrl == null) {
			try {
				redirectUrl = OwaNotifier.getInstance().getProps().getProperty("redirectUrl");
				redirectUrl = redirectUrl.replace("8080", listenPort + "");
			} catch (Exception e) {
				return null;
			}
		}
		return redirectUrl;
	}

	/**
	 * Return scopes
	 * @return
	 * 		A string contains scopes to request on microsoft 
	 */
	private static String getScopes() {
		StringBuilder sb = new StringBuilder();
		for (String scope : scopes) {
			sb.append(scope + " ");
		}
		return sb.toString().trim();
	}

	/**
	 * Return the login url with all parameters to open in broswer
	 * @param state a random uuid
	 * @param nonce a random uuid
	 * @param listenPort the listen port of returnUrl
	 * @return
	 * 		the login url
	 * @throws NumberFormatException
	 * 		In case of listenPort is not a string in properties
	 * @throws IOException
	 * 		In case of exception while reading properties
	 */
	public static String getLoginUrl(UUID state, UUID nonce, int listenPort)
			throws NumberFormatException, IOException {

		StringBuilder urlBuilder = new StringBuilder(authorizeUrl);
		urlBuilder.append("?client_id=" + getAppId());
		urlBuilder.append("&redirect_uri=" + getRedirectUrl());
		urlBuilder.append("&response_type=" + "code%20id_token");
		urlBuilder.append("&scope=" + getScopes().replaceAll(" ", "%20"));
		urlBuilder.append("&state=" + state);
		urlBuilder.append("&nonce=" + nonce);
		// Response mode is
		//urlBuilder.append("&response_mode=" + "form_post");

		return urlBuilder.toString();
	}

	/**
	 * Request token api to refresh the identification token
	 * 	When token have expired, we need to refresh token
	 * @param tokens
	 * 	The initial token object
	 * @param tenantId
	 * 	The id of daemon instance
	 * @return
	 * 	A valid tokenResponse in case of success, an token response with error in case of error
	 */
	public static TokenResponse getTokenFromRefresh(TokenResponse tokens,
			String tenantId) {
		TokenService tokenService = RestfullAcessProxy.getTokenService(authority);
		logger.info("Request to refresh token");
		try {
			JacksonConverter c = new JacksonConverter(new ObjectMapper());
			return (TokenResponse) c.fromBody(
					tokenService.getAccessTokenFromRefreshToken(tenantId,
							getAppId(), getAppPassword(), "refresh_token",
							tokens.getRefreshToken(), getRedirectUrl())
							.getBody(), TokenResponse.class);
		} catch (IOException e) {
			TokenResponse error = new TokenResponse();
			logger.error("IOException in refresh token : ", e);
			error.setError("IOException");
			error.setErrorDescription(e.getMessage());
			return error;
		} catch (retrofit.RetrofitError e) {
			TokenResponse error = new TokenResponse();
			logger.error("API Calling Exeption in refresh token : ", e);
			error.setError("RetrofitError");
			error.setErrorDescription(e.getMessage());
			return error;
		}
	}

	/**
	 * Initial request to get Token from authentication return
	 * @param authCode
	 * 		The authCode getting from mini-webserver
	 * @param tenantId
	 * 		The tenantId getting from mini-webserver
	 * @return
	 * 		A valid token response in case of success, a TokenResponse with error in other case
	 */
	public static TokenResponse getTokenFromAuthCode(String authCode,
			String tenantId) {
		TokenService tokenService = RestfullAcessProxy.getTokenService(authority);
		
		try {
			JacksonConverter c = new JacksonConverter(new ObjectMapper());
			return (TokenResponse) c.fromBody(
					tokenService.getAccessTokenFromAuthCode(tenantId,
							getAppId(), getAppPassword(), "authorization_code",
							authCode, getRedirectUrl()).getBody(),
					TokenResponse.class);
		} catch (IOException e) {
			TokenResponse error = new TokenResponse();
			logger.error("IOException in getting inital token : ", e);
			error.setError("IOException");
			error.setErrorDescription(e.getMessage());
			return error;
		} catch (retrofit.RetrofitError e) {
			TokenResponse error = new TokenResponse();
			logger.error("API Calling Exeption in getting inital token : ", e);
			error.setError("RetrofitError");
			error.setErrorDescription(e.getMessage());
			return error;
		}
	}

}