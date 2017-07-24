package info.kapable.utils.owanotifier.service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.UUID;

import com.squareup.okhttp.OkHttpClient;

import info.kapable.utils.owanotifier.OwaNotifier;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;

public class OutlookServiceBuilder {

	public static OutlookService getOutlookService(String accessTokenSource, String userEmailSource) {
		// Create a request interceptor to add headers that belong on
		// every request
		final String userEmail = userEmailSource;
		final String accessToken = accessTokenSource;
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addHeader("User-Agent", "java-tutorial");
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
		String proxy = OwaNotifier.props.getProperty("proxyHost");
		if(proxy != null && !proxy.contentEquals("")) {
			int proxyPort = Integer.parseInt(OwaNotifier.props.getProperty("proxyPort", "0"));
			if(proxyPort != 0) {
				client.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, proxyPort)));
			}
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
						System.out.println(msg);
					}
				}).setClient(new OkClient(client)).build();

		// Generate the token service
		return restAdapter.create(OutlookService.class);
	}
}