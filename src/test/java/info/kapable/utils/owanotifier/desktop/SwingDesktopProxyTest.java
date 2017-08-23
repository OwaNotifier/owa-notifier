package info.kapable.utils.owanotifier.desktop;

import static org.junit.Assert.fail;
import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.service.Folder;

import org.junit.Test;

public class SwingDesktopProxyTest {

	@Test
	public void test() {
		SwingDesktopProxy s = new SwingDesktopProxy();
		
		Folder folder = new Folder();
		InboxChangeEvent event = new InboxChangeEvent(folder , 1, "hello world");
		try {
			s.processEvent(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("IOException");
		}
	}

}
