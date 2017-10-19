package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void createMemory(){
        authorId = UUID.randomUUID().toString();
        text = "Hello, world!";
        location = "USA, NY";
        memory = new Memory(authorId, text, location);
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
    public void setText() throws Exception {
        String textEdited = "Bye!";
        memory.setText(textEdited);
        assertEquals(textEdited, memory.getText());
    }

    @Test
    public void commentListShouldBeEmpty() throws Exception{
        assertTrue(memory.getComments().isEmpty());
    }
    @Test
    public void getComments() throws Exception {
        assertTrue(memory.getComments()!= null);
    }

    @Test
    public void getPrivacy() throws Exception {
        assertEquals(Memory.Privacy.Public, memory.getPrivacy());
    }

    @Test
    public void setPrivacy() throws Exception {
        memory.setPrivacy(Memory.Privacy.Private);
        assertEquals(Memory.Privacy.Private, memory.getPrivacy());
    }

    @Test
    public void getReminder() throws Exception {
        assertEquals(true, memory.getReminder());
    }

    @Test
    public void setReminder() throws Exception {
        memory.setReminder(false);
        assertEquals(false, memory.getReminder());
    }

}