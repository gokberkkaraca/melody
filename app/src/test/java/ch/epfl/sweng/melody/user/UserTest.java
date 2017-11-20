package ch.epfl.sweng.melody.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import ch.epfl.sweng.melody.memory.Memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
    private User user;

    @Before
    public void setUp() throws Exception {

        final GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
        when(googleSignInAccount.getId()).thenReturn("jiacheng.xu@epfl.ch");
        when(googleSignInAccount.getDisplayName()).thenReturn("Jiacheng Xu");
        when(googleSignInAccount.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
        user = new User(googleSignInAccount);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(user.getId(), "jiacheng,xu@epfl,ch");
    }

    @Test
    public void getDisplayName() throws Exception {
        assertEquals(user.getDisplayName(), "Jiacheng Xu");
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals(user.getEmail(), "jiacheng.xu@epfl.ch");
    }

    @Test
    public void getMemories() throws Exception {
        assertEquals(user.getMemories(), new ArrayList<Memory>());
    }

    @Test
    public void getFriends() throws Exception {
        assertEquals(user.getFriends(), new ArrayList<User>());
    }

    @Test
    public void getFollowers() throws Exception {
        assertEquals(user.getFollowers(), new ArrayList<User>());
    }

    @Test
    public void getFollowings() throws Exception {
        assertEquals(user.getFollowings(), new ArrayList<User>());
    }

    @Test
    public void getProfilePhotoUrl() throws Exception {
        assertEquals(user.getProfilePhotoUrl(), defaultProfilePhotoUrl);
    }

    @Test
    public void getDefaultProfilePhotoURL() throws Exception {
        assertEquals(user.getDefaultProfilePhotoUrl(), defaultProfilePhotoUrl);
    }

    @Test
    public void emptyConstructor() throws Exception {
        assertNotNull(new User());
    }

}