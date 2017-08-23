package info.kapable.utils.owanotifier.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OutlookUserTest {

	@Test
	public void test() {
		OutlookUser u = new OutlookUser();
		u.setId("john");
		u.setMail("test@example.com");
		u.setDisplayName("John Do");
		
		assertTrue(u.getId().contentEquals("john"));
		assertTrue(u.getMail().contentEquals("test@example.com"));
		assertTrue(u.getDisplayName().contentEquals("John Do"));
	}

}
