package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.melody.user.UserContactInfo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    private final String memoryId = Long.toString(System.currentTimeMillis());
    private Comment comment;
    private UserContactInfo sample_user = new UserContactInfo("commentUser1", "SampleUser", "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/resources%2F1511445418787.jpg?alt=media&token=79ef569d-b65a-47b6-b1b9-3b32098153ff", "sample@gmail.com");

    @Before
    public void createComment() {
        comment = new Comment(memoryId, sample_user, "Test comment");
    }

    @Test
    public void getId() throws Exception {
        assertFalse(comment.getId().isEmpty());
    }

    @Test
    public void getMemory() throws Exception {
        assertEquals(comment.getMemoryId(), memoryId);
    }

    @Test
    public void getContent() throws Exception {
        assertTrue(comment.getContent().equals("Test comment"));
    }

    @Test
    public void getTime() throws Exception {
        assertTrue(comment.getTime() != null);
    }

    @Test
    public void getUserContactInfoTest() {
        assertTrue(comment.getUserContactInfo() == sample_user);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createCommentWithNullUser() {
        Comment comment = new Comment(memoryId, null, "Test comment");
    }
}