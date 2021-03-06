package ch.epfl.sweng.melody.memory;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryTest {

    private final String memoryId = Long.toString(System.currentTimeMillis());
    private final String commentId = UUID.randomUUID().toString();
    private final Date time = Calendar.getInstance().getTime();
    private final String text = "Test text";
    private final SerializableLocation serializableLocation = new SerializableLocation(46.5197, 6.6323, "Lausanne");
    private final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/tests%2F1508935737477.mp4?alt=media&token=5a33aae6-a8c6-46c1-9add-181b0ef258c3";
    private final String testPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
    private Memory.Privacy memoryPrivacy = Memory.Privacy.PUBLIC;
    private User user;
    private Memory memory;
    private Memory memoryFromBuilder;

    @Before
    public void createMemory() {
        final FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn("Jiacheng Xu");
        when(firebaseUser.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
        user = new User(firebaseUser);

        final Comment comment = mock(Comment.class);
        final UserContactInfo sample_user = mock(UserContactInfo.class);
        when(comment.getUserContactInfo()).thenReturn(sample_user);
        when(comment.getContent()).thenReturn("Test comment");
        when(comment.getMemoryId()).thenReturn(memoryId);
        when(comment.getTime()).thenReturn(new Date());
        when(comment.getId()).thenReturn(commentId);

        memory = mock(Memory.class);
        when(memory.getId()).thenReturn(memoryId);
        when(memory.getUser()).thenReturn(user);
        when(memory.getTime()).thenReturn(time);
        when(memory.getReminder()).thenReturn(true);
        memoryFromBuilder = new Memory.MemoryBuilder(user, text, serializableLocation, memoryPrivacy)
                .photo(testPhotoUrl)
                .video(testVideoUrl)
                .comments(Collections.singletonMap("123", comment))
                .build();
    }

    @Test
    public void emptyConstructor() throws Exception {
        assertNotNull(new Memory());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(memoryId, memory.getId());
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(user, memory.getUser());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(serializableLocation, memoryFromBuilder.getSerializableLocation());
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
    public void getTags() throws Exception {
        assertTrue(memoryFromBuilder.getTags() != null);
    }

    @Test
    public void getPrivacy() throws Exception {
        assertEquals(Memory.Privacy.PUBLIC, memoryFromBuilder.getPrivacy());
    }

    @Test
    public void getReminder() throws Exception {
        assertEquals(true, memory.getReminder());
    }

    @Test
    public void getMemory() throws Exception {
        assertEquals(memoryFromBuilder, memoryFromBuilder.getMemory());
    }

    @Test
    public void getPhotoUrl() throws Exception {
        assertEquals(testPhotoUrl, memoryFromBuilder.getPhotoUrl());
    }

    @Test
    public void getVideoUrl() throws Exception {
        assertEquals(testVideoUrl, memoryFromBuilder.getVideoUrl());
    }

    @Test
    public void getTime() throws Exception {
        assertEquals(time, memory.getTime());
    }

    @Test
    public void upload() throws Exception {
        assertEquals(memoryFromBuilder.getId(), memoryFromBuilder.upload().getId());
    }
}