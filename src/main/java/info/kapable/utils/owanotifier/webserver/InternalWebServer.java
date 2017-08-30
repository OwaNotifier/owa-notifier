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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
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
		List<Socket> sockets = new ArrayList<Socket>();
		while (this.tokenResponse == null) {
			Socket s;
			try {
				s = this.socket.accept();
				sockets.add(s);
				InternalWebServerTransaction c = new InternalWebServerTransaction(s,this, nonce.toString());
				c.addObserver(this);
			} catch (IOException e) {
				if(e instanceof SocketException) {
					// When user obtain a token closing listen socket cause this exception
					// In this case this is a normal exception
					logger.info("Closing listening socket.");
				} else {
					logger.error("IOException while accepting client requests", e);
				}
				// Close all transcation sockets
				for(Socket transactionSocket: sockets) {
					try {
						if(!transactionSocket.isClosed()) {
							transactionSocket.close();
						}
					} catch (IOException e1) {
						logger.error("IOException while closing client requests", e1);
					}
				}
				serverThread.interrupt();
			}
        }		
		serverThread.interrupt();
	}

	@Override
	public void update(Observable o, Object arg) {
		idTokenObj = (IdToken) arg;
		InternalWebServerTransaction transaction = (InternalWebServerTransaction) o;
		if (idTokenObj != null) {
			tokenResponse = AuthHelper.getTokenFromAuthCode(transaction.code, idTokenObj.getTenantId());
			if(tokenResponse.getError() == null) {
				logger.info("User has a valid token");
				
				try {
					logger.info("Sopping listening thread");
					this.socket.close();
				} catch (IOException e) {
					logger.error("IOException durring ", e);
				}
				this.setChanged();
				this.notifyObservers(tokenResponse);
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
