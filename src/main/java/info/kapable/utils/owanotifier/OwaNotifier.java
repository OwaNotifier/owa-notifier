package info.kapable.utils.owanotifier;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.kapable.utils.owanotifier.auth.AuthHelper;
import info.kapable.utils.owanotifier.auth.TokenResponse;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.service.OutlookService;
import info.kapable.utils.owanotifier.service.OutlookServiceBuilder;
import info.kapable.utils.owanotifier.service.OutlookUser;

public class OwaNotifier implements Observer {
	public static TokenResponse tokenResponse;
	public static TrayIcon trayIcon;

	public static void main(String[] args) throws IOException, URISyntaxException {
		OwaNotifier on = new OwaNotifier();

		System.out.println("Hello World");
		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();

		String loginUrl = AuthHelper.getLoginUrl(state, nonce);
		System.out.println("loginUrl:" + loginUrl);

		// Start browser
		if (Desktop.isDesktopSupported()) {
			Desktop dt = Desktop.getDesktop();
			if (dt.isSupported(Desktop.Action.BROWSE)) {
				URL f = new URL(loginUrl);
				dt.browse(f.toURI());
			}
		}

		ServerSocket serverSocket = new ServerSocket(8080); // Start, listen on
															// port 80
		try {
			Socket s = serverSocket.accept(); // Wait for a client to connect
			ClientHandler c = new ClientHandler(s, nonce.toString(), on); // Handle
																			// the
																			// client
																			// in
																			// a
																			// separate
																			// thread

		} catch (Exception x) {
			System.out.println(x);
		}
	}

	private String email;

	public void displayTray(String message) throws AWTException, java.net.MalformedURLException {
		trayIcon.displayMessage("Nouveaux Messages", message, MessageType.INFO);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("OK ---");
		int lastUnreadCount = 0;

		// Obtain only one instance of the SystemTray object
		SystemTray tray = SystemTray.getSystemTray();

		try {
			// If the icon is a file
			Image image = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));

			// Alternative (if the icon is on the classpath):
			// Image image =
			// Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));
			trayIcon = new TrayIcon(image, "Emails");
			// Let the system resizes the image if needed
			trayIcon.setImageAutoSize(true);
			// Set tooltip text for the tray icon
			trayIcon.setToolTip("Notification de nouveaux courriel(s)");
			trayIcon.addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			        if (e.getClickCount() == 1) {
						Desktop dt = Desktop.getDesktop();
						URL f;
						try {
							f = new URL("https://outlook.office365.com/owa/");
							dt.browse(f.toURI());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        }
			    }
			}); 
			tray.add(trayIcon);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			try {
				while (true) {

					Thread.sleep(5000);
					OutlookService outlookService = OutlookServiceBuilder
							.getOutlookService(this.tokenResponse.getAccessToken(), email);
					JacksonConverter c = new JacksonConverter(new ObjectMapper());
					OutlookUser user = (OutlookUser) c.fromBody(outlookService.getCurrentUser().getBody(),
							OutlookUser.class);

					this.email = user.getMail();
					System.out.println(email);

					// Retrieve messages from the inbox
					String folder = "inbox";
					Folder inbox = (Folder) c.fromBody(outlookService.getFolder(folder).getBody(), Folder.class);
					if (inbox.getUnreadItemCount() > 0 && lastUnreadCount != inbox.getUnreadItemCount()) {
						if (SystemTray.isSupported()) {
							try {
								this.displayTray(inbox.getUnreadItemCount() + " message(s) non lu");
							} catch (AWTException e) { // TODO Auto-generated
														// catch
								e.printStackTrace();
							}
						} else {
							System.err.println("System tray not supported!");
						}
					}
					lastUnreadCount = inbox.getUnreadItemCount();
					trayIcon.setToolTip(lastUnreadCount + " message(s) non lu");

					Thread.sleep(5000);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(255);
		}
		System.exit(0);
	}
}