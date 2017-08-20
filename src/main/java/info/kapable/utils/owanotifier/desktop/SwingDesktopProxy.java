package info.kapable.utils.owanotifier.desktop;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.notification.NotificationFactory;
import com.notification.NotificationFactory.Location;
import com.notification.manager.SimpleManager;
import com.notification.types.IconNotification;

import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.OwaNotifier;
import info.kapable.utils.owanotifier.theme.ThemePackagePresets;
import info.kapable.utils.owanotifier.utils.Time;

public class SwingDesktopProxy extends DesktopProxy {

	@Override
	protected void processEvent(InboxChangeEvent event) throws IOException {
		SimpleManager fade = new SimpleManager(Location.SOUTHEAST);
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());

		// The notification window : 
		final IconNotification icon = factory.buildIconNotification(
				"De: " + event.getEventFrom(),
				event.getEventTitle(),
				event.getEventText(),
				new ImageIcon(this.icon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		
		// Display it :
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

}
