package ch.epfl.sweng.melody.memory;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.user.User;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryUploaderTest {

    private final String memoryId = UUID.randomUUID().toString();
    private final String commentId = UUID.randomUUID().toString();

    User user;
    private MemoryUploader memoryUploader;
    private Memory memory;

    @Before
    public void setUp() {

        final FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn("Jiacheng Xu");
        when(firebaseUser.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
        user = new User(firebaseUser);

        final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/tests%2F1508935737477.mp4?alt=media&token=5a33aae6-a8c6-46c1-9add-181b0ef258c3";
        final String testPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";

        final Comment comment = mock(Comment.class);
        when(comment.getContent()).thenReturn("Test comment");
        when(comment.getMemoryId()).thenReturn(memoryId);
        when(comment.getTime()).thenReturn(new Date());
        when(comment.getId()).thenReturn(commentId);

        memory = mock(Memory.class);
        when(memory.getId()).thenReturn(memoryId);
        when(memory.getUser()).thenReturn(user);
        when(memory.getTime()).thenReturn(new Date());
        when(memory.getText()).thenReturn("Test text");
        when(memory.getComments()).thenReturn(Collections.singletonMap("123", comment));
        when(memory.getPrivacy()).thenReturn(Memory.Privacy.PUBLIC);
        when(memory.getReminder()).thenReturn(true);
        when(memory.getPhotoUrl()).thenReturn(testPhotoUrl);
        when(memory.getVideoUrl()).thenReturn(testVideoUrl);
        when(memory.getSerializableLocation()).thenReturn(new SerializableLocation(46.5197, 6.6323, "Lausanne"));

        memoryUploader = new MemoryUploader(memory);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(memoryUploader.getUser(), memory.getUser());
    }

    @Test
    public void getTime() throws Exception {
        assertEquals(memoryUploader.getTime(), memory.getTime());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(memoryUploader.getSerializableLocation(), memory.getSerializableLocation());
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
        assertEquals(memoryUploader.getPhotoUrl(), memory.getPhotoUrl());
    }

    @Test
    public void getVideoUrl() throws Exception {
        assertEquals(memoryUploader.getVideoUrl(), memory.getVideoUrl());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(memoryUploader.getId(), memory.getId());
    }

    @Test
    public void getMemoryType() throws Exception {
        assertEquals(memoryUploader.getMemoryType(), memory.getMemoryType());
    }
}