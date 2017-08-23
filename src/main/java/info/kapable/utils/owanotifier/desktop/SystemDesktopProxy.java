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
 */package info.kapable.utils.owanotifier.desktop;

import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.OwaNotifier;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemDesktopProxy extends DesktopProxy {

	private TrayIcon trayIcon;

	private String toolTip;
	
	// The logger
    private static Logger logger = LoggerFactory.getLogger(SystemDesktopProxy.class);
	
	/**
	 * On build load icon.png
	 */
	public SystemDesktopProxy() {
		super();

		try {
			Image image = ImageIO.read(getClass().getClassLoader().getResource("icon-waiting.png"));
			trayIcon = new TrayIcon(image, "Emails");
			// Let the system resizes the image if needed
			trayIcon.setImageAutoSize(true);
			// Set tooltip text for the tray icon
			
			this.setToolTip("Notification de nouveaux courriel(s)");
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
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			OwaNotifier.exit(105);
		}

		try {
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (AWTException e1) {
			e1.printStackTrace();
			OwaNotifier.exit(106);
		}
	}

	/**
	 * Update toolTipMessage
	 * @param toolTipMessage
	 */
	private void setToolTip(String toolTipMessage) {
		this.toolTip = toolTipMessage;
		trayIcon.setToolTip(toolTipMessage);
	}
	
	/**
	 * Return the toolTipMessage
	 * @return
	 * 		A string display on tray
	 */
	public String getToolTip() {
		return this.toolTip;
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
				logger.error("Exit Perform");
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
		if (SystemTray.isSupported()) {
			trayIcon.setImage(this.icon);

			if(event.getUnreadItemCount() > 0) {
				this.setToolTip(event.getUnreadItemCount() + " message(s) non lu");
			} else {
				this.setToolTip("Pas de message non lu");
			}
			if(OwaNotifier.getProps().getProperty("notification.type").contentEquals("system")) {
				trayIcon.displayMessage(event.getEventTitle(), event.getEventText(), MessageType.INFO);
			}
		} else {
			logger.error("System tray not supported!");
		}
		
	}
}
