package info.kapable.utils.owanotifier.desktop;

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
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.OwaNotifier;

public class SystemDesktopProxy extends DesktopProxy {

	private TrayIcon trayIcon;
	
	/**
	 * On build load icon.png
	 */
	public SystemDesktopProxy() {
		super();
		try {
			SystemTray tray = SystemTray.getSystemTray();


			// If the icon is a file
			Image image = ImageIO.read(getClass().getClassLoader().getResource("icon-waiting.png"));
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
			trayIcon.setPopupMenu(this.getPopupMenu());
			tray.add(trayIcon);
		} catch (IOException e) {
			e.printStackTrace();
			OwaNotifier.exit(105);
		} catch (AWTException e1) {
			e1.printStackTrace();
			OwaNotifier.exit(106);
		}
	}

	private PopupMenu getPopupMenu() {
		final PopupMenu popup = new PopupMenu();

		// Create a pop-up menu components
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Lien: https://github.com/matgou/owa-notifier", "OwaNotifier: ",
						JOptionPane.INFORMATION_MESSAGE);

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

		// Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);
		return popup;
	}

	@Override
	protected void processEvent(InboxChangeEvent event) throws IOException {
		// TODO Auto-generated method stub
		if (SystemTray.isSupported() &&
				event.getEventType() != InboxChangeEvent.TYPE_LESS_NEW_MSG) {
			trayIcon.setImage(this.icon);
			trayIcon.setToolTip(event.getUnreadItemCount() + " message(s) non lu");
			if(OwaNotifier.getProps().getProperty("notification.type").contentEquals("system")) {
				trayIcon.displayMessage(event.getEventTitle(), event.getEventText(), MessageType.INFO);
			}
		} else {
			OwaNotifier.log("System tray not supported!");
		}
		
	}
}
