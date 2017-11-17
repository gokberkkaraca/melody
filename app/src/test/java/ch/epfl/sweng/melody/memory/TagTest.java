package ch.epfl.sweng.melody.memory;


import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TagTest {

    private final String memoryId = Long.toString(System.currentTimeMillis());
    private Tag tag;

    @Before
    public void createTag() {
        tag = new Tag("SWENG");
    }

    @Test
    public void getId() throws Exception {
        assertFalse(tag.getId().isEmpty());
    }

    @Test
    public void getContent() throws Exception {
        assertTrue(tag.getContent().equals("SWENG"));
    }

}