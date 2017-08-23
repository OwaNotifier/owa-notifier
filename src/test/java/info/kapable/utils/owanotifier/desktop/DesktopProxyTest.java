package info.kapable.utils.owanotifier.desktop;

import junit.framework.TestCase;

import org.junit.Test;

public class DesktopProxyTest extends TestCase {

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

}
