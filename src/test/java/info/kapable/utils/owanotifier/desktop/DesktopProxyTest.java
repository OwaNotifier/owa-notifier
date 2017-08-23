package info.kapable.utils.owanotifier.desktop;

import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.service.Folder;

import java.io.IOException;
import java.util.Observable;

import junit.framework.TestCase;

import org.junit.Test;

public class DesktopProxyTest extends TestCase {
	public static InboxChangeEvent event;

	@Test
	public void testBroswe() {
		try {
			DesktopProxy.browse("http://www.google.com");
		} catch (Exception e) {
			e.printStackTrace();
			fail("MalformedURLException");
		}
		assertTrue(true);
	}

	@Test
	public void testEventPocess() {
		DesktopProxy c = new DesktopProxy() {
			@Override
			protected void processEvent(InboxChangeEvent event)
					throws IOException {
				DesktopProxyTest.event = event;
			};
		};
		Folder folder = new Folder();
		folder.setUnreadItemCount(1);
		InboxChangeEvent arg = new InboxChangeEvent(folder,
				InboxChangeEvent.TYPE_MANY_NEW_MSG);

		c.update(new Observable(), arg);
		assertTrue(DesktopProxyTest.event.getEventTitle().contains(
				arg.getEventTitle()));
	}

}
