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
import info.kapable.utils.owanotifier.notification.manager.SimpleManager;
import info.kapable.utils.owanotifier.theme.ThemePackagePresets;
import info.kapable.utils.owanotifier.utils.Time;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.notification.NotificationFactory;
import com.notification.NotificationFactory.Location;
import com.notification.types.IconNotification;

public class SwingDesktopProxy extends DesktopProxy {
	IconNotification notification;
	private Image icon;

	public SwingDesktopProxy() {
		super();
		try {
			this.icon = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void processEvent(InboxChangeEvent event) throws IOException {
		SimpleManager fade = new SimpleManager(Location.SOUTHEAST);
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());

		// The notification window :
		if(event.getEventType() == InboxChangeEvent.TYPE_ONE_NEW_MSG) {
			notification = factory.buildIconNotification(
				"De: " + event.getEventFrom(),
				event.getEventTitle(),
				event.getEventText(),
				new ImageIcon(this.icon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		} else if (event.getEventType() == InboxChangeEvent.TYPE_MANY_NEW_MSG) {
			notification = factory.buildIconNotification(
					null,
					event.getEventTitle(),
					event.getEventText(),
					new ImageIcon(this.icon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		} else {
			return;
		}
		// Display it :
		try {
			fade.addNotification(notification, Time.seconds(Integer.parseInt(OwaNotifier.getInstance().getProps().getProperty("notification.fade_time"))));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the notification
	 */
	public IconNotification getNotification() {
		return notification;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(IconNotification notification) {
		this.notification = notification;
	}
}
