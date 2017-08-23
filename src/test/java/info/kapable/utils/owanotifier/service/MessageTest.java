/**
The MIT License (MIT)

Copyright (c) 2017 Mathieu GOULIN

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */package info.kapable.utils.owanotifier.service;

import static org.junit.Assert.assertTrue;

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
