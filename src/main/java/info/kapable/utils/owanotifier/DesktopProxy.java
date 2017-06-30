package info.kapable.utils.owanotifier;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import info.kapable.utils.owanotifier.service.Folder;

public class DesktopProxy implements Observer {

	private TrayIcon trayIcon;
	private Image icon;

	/**
	 * Init the desktop proxy to interface with desktop
	 */
	public DesktopProxy() {
		this.initTray();
		try {
			this.icon = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start browser to go on url
	 * 
	 * @param url
	 *            the page web to follow
	 * @throws MalformedURLException
	 *             in case of url is not an url
	 */
	public void browse(String url) throws MalformedURLException {
		// Start browser
		if (Desktop.isDesktopSupported()) {
			Desktop dt = Desktop.getDesktop();
			if (dt.isSupported(Desktop.Action.BROWSE)) {
				URL f = new URL(url);
				try {
					dt.browse(f.toURI());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		} else {
			// browser is unsuported
			OwaNotifier.log("Desktop is unsuported");
			OwaNotifier.exit(1);
		}
	}

	/**
	 * Display message in tray
	 * 
	 * @param message
	 * @throws AWTException
	 * @throws java.net.MalformedURLException
	 */
	public void displayTray(String message) throws AWTException, java.net.MalformedURLException {
		trayIcon.displayMessage("Nouveaux Messages", message, MessageType.INFO);
	}

	/**
	 * init Tray
	 */
	public void initTray() {
		// Obtain only one instance of the SystemTray object
		SystemTray tray = SystemTray.getSystemTray();

		try {
	        final PopupMenu popup = new PopupMenu();
	        
	        // Create a pop-up menu components
	        MenuItem aboutItem = new MenuItem("About");
	        aboutItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
			        JOptionPane.showMessageDialog(null, "Lien: https://github.com/matgou/owa-notifier", "OwaNotifier: ", JOptionPane.INFORMATION_MESSAGE);
					
				}
	        	
	        });
	        MenuItem exitItem = new MenuItem("Exit");
	        exitItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					OwaNotifier.log("Exit Perform");
					OwaNotifier.exit(0);
				}
	        });
	       
	        //Add components to pop-up menu
	        popup.add(aboutItem);
	        popup.addSeparator();
	        popup.add(exitItem);
	        
			// If the icon is a file
			Image image = ImageIO.read(getClass().getClassLoader().getResource("icon-waiting.png"));

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
					if (e.getClickCount() == 2) {
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
	        trayIcon.setPopupMenu(popup);
			tray.add(trayIcon);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		Folder inbox = (Folder) arg;
		if (SystemTray.isSupported()) {
			try {
				this.displayTray(inbox.getUnreadItemCount() + " message(s) non lu");
				OwaNotifier.log(inbox.getUnreadItemCount() + " message(s) non lu");
			} catch (AWTException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			OwaNotifier.log("System tray not supported!");
		}
		trayIcon.setImage(this.icon);
		trayIcon.setToolTip(inbox.getUnreadItemCount() + " message(s) non lu");

	}

}
