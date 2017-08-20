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
package info.kapable.utils.owanotifier.desktop;

import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.OwaNotifier;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.theme.ThemePackagePresets;
import info.kapable.utils.owanotifier.utils.Time;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.notification.NotificationFactory;
import com.notification.NotificationFactory.Location;
import com.notification.manager.SimpleManager;
import com.notification.types.IconNotification;

public abstract class DesktopProxy implements Observer {

	protected Image icon;
	
	public DesktopProxy() {
		try {
			this.icon = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
			OwaNotifier.exit(107);
		}
	}

	protected abstract void processEvent(InboxChangeEvent event) throws IOException;
	
	/**
	 * Static method to start browser redirect user to url
	 * 
	 * @param url
	 *            the page web to follow
	 * @throws MalformedURLException
	 *             in case of url is not an url
	 */
	public static void browse(String url) throws MalformedURLException {
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
	public void displayTray(String from, String subject, String message) throws AWTException, java.net.MalformedURLException {
		try {
			if(OwaNotifier.getProps().getProperty("notification.type").contentEquals("system")) {
			} else {
				this.notify(from, subject, message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Send notification to desktop
	 * @param title
	 * @param message
	 * @throws Exception
	 */
	public void notify(String from, String title, String message) {

		SimpleManager fade = new SimpleManager(Location.SOUTHEAST);
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());

		final IconNotification icon = factory.buildIconNotification("De: " + from,title, message,	new ImageIcon(this.icon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		
		
		try {
			fade.addNotification(icon, Time.seconds(Integer.parseInt(OwaNotifier.getProps().getProperty("notification.fade_time"))));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void update(Observable o, Object arg) {
		InboxChangeEvent event = (InboxChangeEvent) arg;
		try {
			this.processEvent(event);
		} catch (IOException e1) {
			e1.printStackTrace();
			OwaNotifier.exit(255);
		}
		if (SystemTray.isSupported() && event.getEventType() != InboxChangeEvent.TYPE_LESS_NEW_MSG) {
			try {
				this.displayTray(event.getEventFrom(), event.getEventTitle(), event.getEventText());
				OwaNotifier.log(event.getEventText());
			} catch (AWTException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} 

	}

}
