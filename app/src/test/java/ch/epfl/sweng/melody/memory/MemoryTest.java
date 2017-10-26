package ch.epfl.sweng.melody.memory;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryTest {

    private Memory memory;
    private Memory memoryFromBuilder;

    private final String memoryId = UUID.randomUUID().toString();
    private final String memoryAuthorId = UUID.randomUUID().toString();
    private final String commentId = UUID.randomUUID().toString();
    private final String commentAuthorId = UUID.randomUUID().toString();
    private final Date time  = Calendar.getInstance().getTime();
    private final String text = "Test text";
    private final String location = "Lausanne";


    private final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/tests%2F1508935737477.mp4?alt=media&token=5a33aae6-a8c6-46c1-9add-181b0ef258c3";
    private final String testPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
    private final String testAudioUrl = "https://fakeaudiourl.com";


    @Before
    public void createMemory() {
        final Comment comment = mock(Comment.class);
        when(comment.getAuthorId()).thenReturn(commentAuthorId);
        when(comment.getContent()).thenReturn("Test comment");
        when(comment.getMemoryId()).thenReturn(memoryId);
        when(comment.getTime()).thenReturn(new Date());
        when(comment.getId()).thenReturn(commentId);

        memory = mock(Memory.class);
        when(memory.getId()).thenReturn(memoryId);
        when(memory.getAuthorId()).thenReturn(memoryAuthorId);
        when(memory.getTime()).thenReturn(time);
        when(memory.getPrivacy()).thenReturn(Memory.Privacy.PUBLIC);
        when(memory.getReminder()).thenReturn(true);
        memoryFromBuilder = new Memory.MemoryBuilder(memoryAuthorId,text,location)
                .photo(testPhotoUrl)
                .video(testVideoUrl)
                .audio(testAudioUrl)
                .comments(Collections.singletonList(comment))
                .build();
    }

    @Test
    public void emptyConstructor() throws Exception {
        assertNotNull(new Memory());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(memoryId,memory.getId());
    }

    @Test
    public void getAuthor() throws Exception {
        assertEquals(memoryAuthorId, memory.getAuthorId());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(location, memoryFromBuilder.getLocation());
    }

    @Test
    public void getText() throws Exception {
        assertEquals(text, memoryFromBuilder.getText());
    }

    @Test
    public void getComments() throws Exception {
        assertTrue(memoryFromBuilder.getComments() != null);
    }

    @Test
    public void getPrivacy() throws Exception {
        assertEquals(Memory.Privacy.PUBLIC, memory.getPrivacy());
    }

    @Test
    public void getReminder() throws Exception {
        assertEquals(true, memory.getReminder());
    }

    @Test
    public void getMemoryType() throws Exception{
        assertEquals(Memory.MemoryType.AUDIO,memoryFromBuilder.getMemoryType());
    }

    @Test
    public void getMemory() throws Exception{
        assertEquals(memoryFromBuilder,memoryFromBuilder.getMemory());
    }

    @Test
    public void getPhoto() throws Exception{
        assertEquals(testPhotoUrl,memoryFromBuilder.getPhoto());
    }

    @Test
    public void getAudioUrl() throws Exception{
        assertEquals(testAudioUrl,memoryFromBuilder.getAudioUrl());
    }

    @Test
    public void getViedoUrl() throws Exception{
        assertEquals(testVideoUrl,memoryFromBuilder.getVideoUrl());
    }

    @Test
    public void getTime() throws Exception{
        assertEquals(time,memory.getTime());
    }

    @Test
    public void upload() throws Exception{
        assertEquals(memoryFromBuilder.getId(),memoryFromBuilder.upload().getId());
    }
}