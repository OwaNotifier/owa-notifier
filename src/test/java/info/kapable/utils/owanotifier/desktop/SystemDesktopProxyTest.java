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
 */package info.kapable.utils.owanotifier.desktop;

import static org.junit.Assert.fail;
import info.kapable.utils.owanotifier.InboxChangeEvent;
import info.kapable.utils.owanotifier.service.Folder;

import org.junit.Test;

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
