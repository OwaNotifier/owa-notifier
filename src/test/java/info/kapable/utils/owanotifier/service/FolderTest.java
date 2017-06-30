package info.kapable.utils.owanotifier.service;

import static org.junit.Assert.*;

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
