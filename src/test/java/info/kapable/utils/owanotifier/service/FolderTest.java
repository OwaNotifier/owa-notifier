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

import org.junit.Test;

public class FolderTest {

	@Test
	public void test() {
		Folder f = new Folder();
		f.setId("inbox");
		f.setChildFolderCount(3);
		f.setParentFolderId("root");
		f.setTotalItemCount(10);
		f.setUnreadItemCount(3);
		
		assertTrue(f.getId().contentEquals("inbox"));
		assertTrue(f.getChildFolderCount() == 3);
		assertTrue(f.getParentFolderId().contentEquals("root"));
		assertTrue(f.getTotalItemCount() == 10);
		assertTrue(f.getUnreadItemCount() == 3);
	}

}
