package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class MemoryPhotoTest {

    private String author;
    private String text;
    private String location;
    private List<String> photos;
    private MemoryPhoto memoryPhoto;

    @Before
    public void createMemoryAudio() throws Exception {
        author = UUID.randomUUID().toString();
        text = "Example test";
        location = "Switzerland, Lausanne";
        photos = new ArrayList<>();
        photos.add("http://www.google.com");
        photos.add("http://www.twitter.com");
        photos.add("http://www.facebook.com");
        memoryPhoto = new MemoryPhoto(author, text, location, photos);
    }

    @Test
    public void getPhotos() throws Exception {
        assertTrue(memoryPhoto.getPhotos().equals(photos));
    }
}