package info.kapable.utils.owanotifier.service;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class MessageTest {

	@Test
	public void test() {
		EmailAddress e = new EmailAddress();
		e.setAddress("test@example.com");
		e.setName("John Do");
		
		Recipient r = new Recipient();
		r.setEmailAddress(e);
		
		Message m = new Message();
		m.setBodyPreview("body preview");
		m.setFrom(r);
		m.setId("message1");
		m.setIsRead(false);
		m.setReceivedDateTime(new Date());
		m.setSubject("subject1");

		assertTrue(m.getBodyPreview().contains("body preview"));
		assertTrue(m.getFrom().getEmailAddress().getAddress().contentEquals("test@example.com"));
		assertTrue(m.getId().contentEquals("message1"));
		assertTrue(m.getIsRead() == false);
		assertTrue(m.getReceivedDateTime().getClass() == Date.class);
		assertTrue(m.getSubject().contentEquals("subject1"));
	}

}
