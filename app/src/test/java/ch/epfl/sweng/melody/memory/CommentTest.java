package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentTest {

    private final String memoryId = Long.toString(System.currentTimeMillis());
    private Comment comment;
    private String authorId = UUID.randomUUID().toString();

    @Before
    public void createComment() {
        comment = new Comment(memoryId,authorId,"Test comment");
    }

    @Test
    public void getId() throws Exception {
        assertFalse(comment.getId().isEmpty());
    }

    @Test
    public void getMemory() throws Exception {
        assertEquals(comment.getMemoryId(),memoryId);
    }

    @Test
    public void getAuthor() throws Exception {
        assertTrue(comment.getAuthorId().equals(authorId));
    }

    @Test
    public void getContent() throws Exception {
        assertTrue(comment.getContent().equals("Test comment"));
    }

    @Test
    public void getTime() throws Exception {
        assertTrue(comment.getTime() != null);
    }

}