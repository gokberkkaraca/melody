package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    private Comment comment;
    private String memoryId;
    private String authorId;
    private String content;

    @Before
    public void createComment() {
        memoryId = UUID.randomUUID().toString();
        authorId = UUID.randomUUID().toString();
        content = "Test comment";
        comment = new Comment(memoryId, authorId, content);
    }

    @Test
    public void getId() throws Exception {
        assertFalse(comment.getId().isEmpty());
    }

    @Test
    public void getMemory() throws Exception {
        assertTrue(comment.getMemoryId().equals(memoryId));
    }

    @Test
    public void getAuthor() throws Exception {
        assertTrue(comment.getAuthorId().equals(authorId));
    }

    @Test
    public void getContent() throws Exception {
        assertTrue(comment.getContent().equals(content));
    }

    @Test
    public void getTime() throws Exception {
        assertTrue(comment.getTime() != null);
    }

}