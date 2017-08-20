package info.kapable.utils.owanotifier.desktop;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.service.Folder;

public class SystemDesktopProxyTest {

	@Test
	public void test() {
		SystemDesktopProxy s = new SystemDesktopProxy();
		
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
