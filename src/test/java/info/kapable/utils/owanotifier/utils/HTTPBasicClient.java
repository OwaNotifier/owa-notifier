package info.kapable.utils.owanotifier.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * A basic HTTP client to test auth
 * 
 * @author Mathieu GOULIN <mathieu.goulin@gadz.org>
 */
public class HTTPBasicClient extends Thread{

	public String output = "";
	public String input = "";
	public String host = "localhost";
	public int responseCode;
	public int listenPort = 8080;
	
	/**
	 *  
	 * @param input input to pass on server
	 * @param listenPort server port
	 */
	public HTTPBasicClient(String input, String host, int listenPort) {
		super();
		this.input = input;
		this.listenPort = listenPort;
		this.host = host;
	}
	
	@Override
	public void run() {
         try {
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	String url = "http://" + this.host + ":" + this.listenPort + "/";

    		URL obj = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    		//add reuqest header
    		con.setRequestMethod("POST");
    		
    		// Send post request
    		con.setDoOutput(true);
    		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    		wr.writeBytes(input);
    		wr.flush();
    		wr.close();

    		BufferedReader in = new BufferedReader(
    		        new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		StringBuffer response = new StringBuffer();

    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    		this.output = response.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
