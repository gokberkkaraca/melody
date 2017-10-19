package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CommentTest {

    Comment comment;
    String memory;
    String author;
    String content;

    @Before
    public void createComment(){
        memory = UUID.randomUUID().toString();
        author = UUID.randomUUID().toString();
        content = "Test comment";
        comment = new Comment(memory, author, content);
    }

    @Test
    public void getMemory() throws Exception {
        assertTrue(comment.getMemory().equals(memory));
    }

    @Test
    public void getAuthor() throws Exception {
        assertTrue(comment.getAuthor().equals(author));
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