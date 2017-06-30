package info.kapable.utils.owanotifier.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecipientTest {

	@Test
	public void test() {
		EmailAddress e = new EmailAddress();
		e.setAddress("test@example.com");
		e.setName("John Do");
		
		Recipient r = new Recipient();
		r.setEmailAddress(e);
		
		assertTrue(r.getEmailAddress().getAddress().contentEquals("test@example.com"));
	}

}
