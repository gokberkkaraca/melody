package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by yusiz on 2017/10/19.
 */
public class MemoryVideoTest {

    private String author;
    private String text;
    private String location;
    private URL videoURL;
    private MemoryVideo memoryVideo;

    @Before
    public void createMemoryVideo() throws Exception {
        author = UUID.randomUUID().toString();
        text = "Example test";
        location = "Switzerland, Lausanne";
        videoURL = new URL("http://example.com");
        memoryVideo = new MemoryVideo(author, text, location, videoURL);
    }

    @Test
    public void getAudio() throws Exception {
        assertTrue(memoryVideo.getVideos().equals(videoURL));
    }
}