package info.kapable.utils.owanotifier.utils;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.notification.Notification;
import com.notification.types.TextNotification;

public class NotificationTest {
	@Test
	public void notificationShouldStartUnmanaged() {
		Notification note = new TextNotification();
		assertFalse("Notification should start unmanaged", note.isManaged());
	}
}
