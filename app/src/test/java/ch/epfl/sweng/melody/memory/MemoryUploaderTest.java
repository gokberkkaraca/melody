package ch.epfl.sweng.melody.memory;


import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryUploaderTest {

    private MemoryUploader memoryUploader;
    private Memory memory;

    private final String memoryId = UUID.randomUUID().toString();
    private final String memoryAuthorId = UUID.randomUUID().toString();
    private final String commentId = UUID.randomUUID().toString();
    private final String commentAuthorId = UUID.randomUUID().toString();

    @Before
    public void setUp(){

        final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/tests%2F1508935737477.mp4?alt=media&token=5a33aae6-a8c6-46c1-9add-181b0ef258c3";
        final String testPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
        final String testAudioUrl = "https://fakeaudiourl.com";

        final Comment comment = mock(Comment.class);
        when(comment.getAuthorId()).thenReturn(commentAuthorId);
        when(comment.getContent()).thenReturn("Test comment");
        when(comment.getMemoryId()).thenReturn(memoryId);
        when(comment.getTime()).thenReturn(new Date());
        when(comment.getId()).thenReturn(commentId);

        memory = mock(Memory.class);
        when(memory.getId()).thenReturn(memoryId);
        when(memory.getAuthorId()).thenReturn(memoryAuthorId);
        when(memory.getTime()).thenReturn(new Date());
        when(memory.getText()).thenReturn("Test text");
        when(memory.getComments()).thenReturn(Collections.singletonList(comment));
        when(memory.getPrivacy()).thenReturn(Memory.Privacy.PUBLIC);
        when(memory.getReminder()).thenReturn(true);
        when(memory.getPhoto()).thenReturn(testPhotoUrl);
        when(memory.getVideoUrl()).thenReturn(testVideoUrl);
        when(memory.getAudioUrl()).thenReturn(testAudioUrl);
        when(memory.getLocation()).thenReturn("Lausanne");

        memoryUploader = new MemoryUploader(memory);
    }

    @Test
    public void getAuthorId() throws Exception {
        assertEquals(memoryUploader.getAuthorId(), memory.getAuthorId());
    }

    @Test
    public void getTime() throws Exception {
        assertEquals(memoryUploader.getTime(), memory.getTime());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(memoryUploader.getLocation(), memory.getLocation());
    }

    @Test
    public void getText() throws Exception {
        assertEquals(memoryUploader.getText(), memory.getText());
    }

    @Test
    public void getComments() throws Exception {
        assertEquals(memoryUploader.getComments(), memory.getComments());
    }

    @Test
    public void getPrivacy() throws Exception {
        assertEquals(memoryUploader.getPrivacy(), memory.getPrivacy());
    }

    @Test
    public void getReminder() throws Exception {
        assertEquals((memoryUploader.getReminder()), memory.getReminder());
    }

    @Test
    public void getPhoto() throws Exception {
        assertEquals(memoryUploader.getPhoto(), memory.getPhoto());
    }

    @Test
    public void getVideoUrl() throws Exception {
        assertEquals(memoryUploader.getVideoUrl(), memory.getVideoUrl());
    }

    @Test
    public void getAudioUrl() throws Exception {
        assertEquals(memoryUploader.getAudioUrl(), memory.getAudioUrl());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(memoryUploader.getId(), memory.getId());
    }
}