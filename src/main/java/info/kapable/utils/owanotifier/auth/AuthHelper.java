package info.kapable.utils.owanotifier.auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.owanotifier.JacksonConverter;
import retrofit.RestAdapter;

public class AuthHelper {
	private static final String authority = "https://login.microsoftonline.com";
	private static final String authorizeUrl = authority + "/common/oauth2/v2.0/authorize";

	private static String[] scopes = { "openid", "offline_access", "profile", "User.Read", "Mail.Read" };

	private static String appId = null;
	private static String appPassword = null;
	private static String redirectUrl = null;

	private static String getAppId() {
		if (appId == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return appId;
	}

	private static String getAppPassword() {
		if (appPassword == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return appPassword;
	}

	private static String getRedirectUrl() {
		if (redirectUrl == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return redirectUrl;
	}

	private static String getScopes() {
		StringBuilder sb = new StringBuilder();
		for (String scope : scopes) {
			sb.append(scope + " ");
		}
		return sb.toString().trim();
	}

	private static void loadConfig() throws IOException {
		String authConfigFile = "auth.properties";
		InputStream authConfigStream = AuthHelper.class.getClassLoader().getResourceAsStream(authConfigFile);

		if (authConfigStream != null) {
			Properties authProps = new Properties();
			try {
				authProps.load(authConfigStream);
				appId = authProps.getProperty("appId");
				appPassword = authProps.getProperty("appPassword");
				redirectUrl = authProps.getProperty("redirectUrl");
			} finally {
				authConfigStream.close();
			}
		} else {
			throw new FileNotFoundException("Property file '" + authConfigFile + "' not found in the classpath.");
		}
	}

	public static String getLoginUrl(UUID state, UUID nonce) {

		StringBuilder urlBuilder = new StringBuilder(authorizeUrl);
		urlBuilder.append("?client_id=" + getAppId());
		urlBuilder.append("&redirect_uri=" + getRedirectUrl());
		urlBuilder.append("&response_type=" + "code%20id_token");
		urlBuilder.append("&scope=" + getScopes().replaceAll(" ", "%20"));
		urlBuilder.append("&state=" + state);
		urlBuilder.append("&nonce=" + nonce);
		urlBuilder.append("&response_mode=" + "form_post");

		return urlBuilder.toString();
	}

	public static TokenResponse getTokenFromAuthCode(String authCode, String tenantId) {
		// Create a logging interceptor to log request and responses
		/*
		 * Client client = new OkHttpClient.Builder() .proxy(new
		 * Proxy(Proxy.Type.HTTP, new InetSocketAddress("100.54.33.159", 3129)))
		 * .build();
		 */

		// Create and configure the Retrofit object
		RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(authority).build();

		// Generate the token service
		TokenService tokenService = retrofit.create(TokenService.class);

		try {
			JacksonConverter c = new JacksonConverter(new ObjectMapper());
			return (TokenResponse) c.fromBody(tokenService.getAccessTokenFromAuthCode(tenantId, getAppId(),
					getAppPassword(), "authorization_code", authCode, getRedirectUrl()).getBody(), TokenResponse.class);
		} catch (IOException e) {
			TokenResponse error = new TokenResponse();
			error.setError("IOException");
			error.setErrorDescription(e.getMessage());
			return error;
		}
	}

}