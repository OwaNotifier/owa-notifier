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

import info.kapable.utils.owanotifier.auth.TokenService;
import info.kapable.utils.owanotifier.service.OutlookService;
import info.kapable.utils.owanotifier.teststubs.StubTokenService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RequestInterceptor.RequestFacade;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;

import com.github.markusbernhardt.proxy.ProxySearch;
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
	 * Return a proxy if exist
	 * @return
	 * 	null if no proxy, the InetSocketAddress else of proxy
	 */
	public static InetSocketAddress findProxy() {
		logger.info("detecting proxies");
		ProxySearch ps = ProxySearch.getDefaultProxySearch();
		ProxySelector myProxySelector = ps.getProxySelector(); 
		ProxySelector.setDefault(myProxySelector);


		List<Proxy> l = null;
		if(myProxySelector == null) {
        	logger.info("No Proxy");
			return null;
		}
		try {
			l = myProxySelector.select(new URI("https://graph.microsoft.com"));
		}  
		catch (URISyntaxException e) {
		    e.printStackTrace();
		}
		if (l != null) {
		    for (Iterator<Proxy> iter = l.iterator(); iter.hasNext();) {
		        java.net.Proxy proxy = (java.net.Proxy) iter.next();
		        logger.info("proxy type: " + proxy.type());

		        InetSocketAddress addr = (InetSocketAddress) proxy.address();
		        if (addr == null) {
		        	logger.info("No Proxy");
		            return null;
		        } 
		        logger.info("Hostname: " + addr.getHostName());
		        logger.info("Port : " + addr.getPort());
		        return addr;
		    }
		}
    	logger.info("No Proxy");
		return null;
	}
	
	public static OutlookService getRealOutlookService(String accessTokenSource, String userEmailSource) {
		// Create a request interceptor to add headers that belong on
		// every request
		final String userEmail = userEmailSource;
		final String accessToken = accessTokenSource;
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addHeader("User-Agent", "owa-notifier-daemon");
				request.addHeader("client-request-id", UUID.randomUUID().toString());
				request.addHeader("return-client-request-id", "true");
				request.addHeader("Authorization", String.format("Bearer %s", accessToken));
				request.addHeader("Accept","application/json");

				if (userEmail != null && !userEmail.isEmpty()) {
					request.addHeader("X-AnchorMailbox", userEmail);
				}
			}
		};
		
		OkHttpClient client = new OkHttpClient();
		InetSocketAddress p = findProxy();
		if(p != null) {
			client.setProxy(new Proxy(Proxy.Type.HTTP,p));
		} else { 
			client.setProxy(Proxy.NO_PROXY); 
		}
		
		// Create and configure the Retrofit object
		RestAdapter restAdapter = new RestAdapter.Builder()
				// Retrofit retrofit = new Retrofit.Builder()
				.setEndpoint("https://graph.microsoft.com")
				/*
				 * .client(client)
				 * .addConverterFactory(JacksonConverterFactory.create())
				 */
				.setRequestInterceptor(requestInterceptor).setLogLevel(LogLevel.FULL).setLog(new RestAdapter.Log() {
					@Override
					public void log(String msg) {
						logger.debug(msg);
					}
				}).setClient(new OkClient(client)).build();

		// Generate the token service
		return restAdapter.create(OutlookService.class);
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
		InetSocketAddress p = findProxy();
		if(p != null) {
			client.setProxy(new Proxy(Proxy.Type.HTTP,p));
		} else { 
			client.setProxy(Proxy.NO_PROXY); 
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

	/**
	 * @return 
	 * 	an instance of outlook service
	 */
	public static OutlookService getOutlookService(String accessTokenSource, String userEmailSource) {
		return getRealOutlookService(accessTokenSource,userEmailSource);
	}

}
