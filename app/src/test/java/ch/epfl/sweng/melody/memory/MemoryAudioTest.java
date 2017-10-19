package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.*;

public class MemoryAudioTest {

    private String author;
    private String text;
    private String location;
    private URL audioURL;
    private MemoryAudio memoryAudio;

    @Before
    public void createMemoryAudio() throws Exception {
        author = UUID.randomUUID().toString();
        text = "Example test";
        location = "Switzerland, Lausanne";
        audioURL = new URL("http://example.com");
        memoryAudio = new MemoryAudio(author, text, location, audioURL);
    }

    @Test
    public void getAudio() throws Exception {
        assertTrue(memoryAudio.getAudio().equals(audioURL));
    }
}