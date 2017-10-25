package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by yusiz on 2017/10/19.
 */
public class MemoryTest {

    String authorId;
    String text;
    String location;
    Memory memory;
    List<Comment> comments = new ArrayList<>();

    @Before
    public void createMemory(){
        authorId = UUID.randomUUID().toString();
        text = "Hello, world!";
        location = "USA, NY";
        comments.add(new Comment("test",authorId,"this is a comment test"));
        memory = new Memory.MemoryBuilder(authorId,text,location).comments(comments).build();

    }

    @Test
    public void getId() throws Exception {
        assertFalse(memory.getId().isEmpty());
    }

    @Test
    public void getAuthor() throws Exception {
        assertEquals(authorId, memory.getAuthor());
    }

    @Test
    public void getTime() throws Exception {
        assertFalse(memory.getId().isEmpty());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(location, memory.getLocation());
    }

    @Test
    public void getText() throws Exception {
        assertEquals(text, memory.getText());
    }

    @Test
    public void getComments() throws Exception {
        assertTrue(memory.getComments()!= null);
    }

    @Test
    public void getPrivacy() throws Exception {
        assertEquals(Memory.Privacy.PUBLIC, memory.getPrivacy());
    }

    @Test
    public void getReminder() throws Exception {
        assertEquals(true, memory.getReminder());
    }
}