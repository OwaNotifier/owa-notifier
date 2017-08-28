package info.kapable.utils.owanotifier.webserver;

import info.kapable.utils.owanotifier.OwaNotifier;
import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.AuthListner;
import info.kapable.utils.owanotifier.auth.IdToken;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalWebServer extends AuthListner implements Runnable, Observer {

	private ServerSocket socket;
	private int listenPort;
	private UUID nonce;
	public static Thread serverThread;
	
	// The logger
    private static Logger logger = LoggerFactory.getLogger(OwaNotifier.class);
	
    /**
     * Build a new instance of InternalWebServer
     * @param nonce 
     * @throws IOException
     */
	public InternalWebServer(UUID nonce) throws IOException {
		this.listenPort = Integer.parseInt(OwaNotifier.getInstance().getProps().getProperty("listenPort", "8080"));
		this.nonce = nonce;
		// Search an available port
		while(this.socket == null) {
			try {
				InetAddress[] IP=InetAddress.getAllByName("localhost");
				if(IP.length < 1) {
					throw new IOException("Unable to find localhost ip");
				}
				this.socket = new ServerSocket(this.listenPort, 10, IP[0]); // Start, listen on port 8080
				logger.info("Use listen " + this.socket.getInetAddress().toString() + ":" + this.listenPort);
				
			} catch (BindException e){
				this.listenPort = this.listenPort +1;
				OwaNotifier.getInstance().setProps("listenPort", this.listenPort + "");
			}
		}

		// detach thread
		serverThread = new Thread(this);
		serverThread.start();
	}

	@Override
	public void run() {
		while (true) {
			Socket s;
			try {
				s = this.socket.accept();
				InternalWebServerTransaction c = new InternalWebServerTransaction(s,this, nonce.toString());
				c.addObserver(this);
			} catch (IOException e) {
				logger.error("IOException while processing client requests", e);
				e.printStackTrace();
			} 
        }		
	}

	@Override
	public void update(Observable o, Object arg) {
		idTokenObj = (IdToken) arg;
		InternalWebServerTransaction transaction = (InternalWebServerTransaction) o;
		if (idTokenObj != null) {
			tokenResponse = AuthHelper.getTokenFromAuthCode(transaction.code, idTokenObj.getTenantId());
			if(tokenResponse.getError() == null) {
				logger.info("User has a valid token");
				
				this.setChanged();
				this.notifyObservers(tokenResponse);
				serverThread.interrupt();
			} else {
				logger.error(tokenResponse.getError());
				logger.error(tokenResponse.getErrorDescription());
				logger.error("User don't have a valid token");
				OwaNotifier.exit(255);
				serverThread.interrupt();
			}
		}
		
	}
}
