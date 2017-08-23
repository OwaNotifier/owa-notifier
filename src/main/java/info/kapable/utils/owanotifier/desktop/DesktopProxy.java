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
import info.kapable.utils.owanotifier.auth.WebserverClientHandler;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notification.NotificationFactory;
import com.notification.NotificationFactory.Location;
import com.notification.manager.SimpleManager;
import com.notification.types.IconNotification;

public abstract class DesktopProxy implements Observer {

	protected Image icon;

	// The logger
    private static Logger logger = LoggerFactory.getLogger(DesktopProxy.class);
	
	public DesktopProxy() {
		try {
			this.icon = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
			OwaNotifier.exit(107);
		}
	}

	/**
	 * This function is call web event is throw 
	 * Display notification on destop
	 * @param event
	 * 			The event to display
	 * @throws IOException
	 * 		In case of exception during event processing
	 */
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
			logger.error("Desktop is unsuported");
			OwaNotifier.exit(1);
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
	}
}
