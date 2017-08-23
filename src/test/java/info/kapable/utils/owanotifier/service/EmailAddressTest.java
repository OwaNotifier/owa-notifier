package info.kapable.utils.owanotifier.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EmailAddressTest {

	@Test
	public void test() {
		EmailAddress e = new EmailAddress();
		e.setAddress("test@example.com");
		e.setName("John Do");
		assertTrue(e.getAddress().contentEquals("test@example.com"));
		assertTrue(e.getName().contentEquals("John Do"));
	}

}
