package info.kapable.utils.owanotifier;

import info.kapable.utils.owanotifier.auth.TokenService;
import info.kapable.utils.owanotifier.teststubs.StubTokenService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;

import com.squareup.okhttp.OkHttpClient;

/**
 * A proxy class to access to retrofit service
 * 
 * @author Mathieu GOULIN
 */
public class RestfullAcessProxy {
	// The logger
    private static Logger logger = LoggerFactory.getLogger(RestfullAcessProxy.class);
    
    // On jUnit context don't present rest api but return stub
    public static boolean stubMode = false;

    /**
     * Return a instance of tokenService to call it
     * 
     * @param authority
     * 		The url to login
     * @return TokenService 
     * 		return a proxy to call api
     */
	public static TokenService getTokenService(String authority) {
		if(!stubMode) {
			return getRealTokenService(authority);
		} else {
			logger.debug("Use stub as TokenService");
			return getStubTokenService();
		}
	}
	
	/**
	 * On jUnit context return a stub to simulate rest TokenService
	 * 
	 * @return TokenService
	 * 		The service to use to call api
	 */
	private static TokenService getStubTokenService() {
		return StubTokenService.getStubTokenService();
	}
	
	/**
	 * Return a instance of tokenService to call it
	 * 
	 * @param authority
	 * @return TokenService return a proxy to call api
	 */
	private static TokenService getRealTokenService(String authority) {
		// Create a logging interceptor to log request and responses
		OkHttpClient client = new OkHttpClient();
		String proxy;
		try {
			proxy = OwaNotifier.getProps().getProperty("proxyHost");
			if (proxy != null && !proxy.contentEquals("")) {
				int proxyPort = Integer.parseInt(OwaNotifier.getProps()
						.getProperty("proxyPort", "0"));
				if (proxyPort != 0) {
					client.setProxy(new Proxy(Proxy.Type.HTTP,
							new InetSocketAddress(proxy, proxyPort)));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Create and configure the Retrofit object
		RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(authority)
				.setLogLevel(LogLevel.FULL).setLog(new RestAdapter.Log() {
					@Override
					public void log(String msg) {
						logger.debug(msg);
					}
				}).setClient(new OkClient(client)).build();

		// Generate the token service
		return retrofit.create(TokenService.class);
	}

}
