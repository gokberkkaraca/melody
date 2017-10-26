package ch.epfl.sweng.melody.memory;


import org.junit.Test;
import org.junit.Before;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryUploaderTest {

    MemoryUploader memoryUploader;

    @Before
    public void setUp(){
        final String memoryId = UUID.randomUUID().toString();
        final String commentId = UUID.randomUUID().toString();

        final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/tests%2F1508935737477.mp4?alt=media&token=5a33aae6-a8c6-46c1-9add-181b0ef258c3";
        final String testPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";

        final Comment comment = mock(Comment.class);
        when(comment.getAuthorId()).thenReturn(commentId);
        when(comment.getContent()).thenReturn("Test comment");
        when(comment.getMemoryId()).thenReturn(memoryId);
        when(comment.getTime()).thenReturn(new Date());
        when(comment.getId()).thenReturn(commentId);

        final Memory memory = mock(Memory.class);
        when(memory.getId()).thenReturn(UUID.randomUUID().toString());
        when(memory.getAuthorId()).thenReturn(memoryId);
        when(memory.getTime()).thenReturn(new Date());
        when(memory.getText()).thenReturn("Test text");
        when(memory.getComments()).thenReturn(Collections.singletonList(comment));
        when(memory.getPrivacy()).thenReturn(Memory.Privacy.PUBLIC);
        when(memory.getReminder()).thenReturn(true);
        when(memory.getPhoto()).thenReturn(testPhotoUrl);
        when(memory.getVideoUrl()).thenReturn(testVideoUrl);

        memoryUploader = new MemoryUploader(memory);
    }

    @Test
    public void getAuthorId() throws Exception {
    }

    @Test
    public void getTime() throws Exception {
    }

    @Test
    public void getLocation() throws Exception {
    }

    @Test
    public void getText() throws Exception {
    }

    @Test
    public void getComments() throws Exception {
    }

    @Test
    public void getPrivacy() throws Exception {
    }

    @Test
    public void getReminder() throws Exception {
    }

    @Test
    public void getPhoto() throws Exception {
    }

    @Test
    public void getVideoUrl() throws Exception {
    }

    @Test
    public void getAudioUrl() throws Exception {
    }

    @Test
    public void getId() throws Exception {
    }
}