package ch.epfl.sweng.melody.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.melody.memory.Memory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void getUserContactInfo() throws Exception {
        UserContactInfo userContactInfo = user.getUserContactInfo();
        UserContactInfo userContactInfo1 = user.getUserContactInfo();
        assertTrue(userContactInfo.equals(userContactInfo1));
        assertThat(userContactInfo.getDisplayName(), is(user.getDisplayName()));
        assertThat(userContactInfo.getEmail(), is(user.getEmail()));
        assertThat(userContactInfo.getProfilePhotoUrl(), is(user.getProfilePhotoUrl()));
        assertThat(userContactInfo.getUserId(), is(user.getId()));
    }

    @Test (expected = IllegalStateException.class)
    public void addFriend() throws Exception {
        UserContactInfo userContactInfo = user.getUserContactInfo();
        user.addFriend(userContactInfo);
        assertThat(user.getFriends().size(), is(1));
        user.addFriend(userContactInfo);
    }

    @Test (expected = IllegalStateException.class)
    public void removeFriend() throws Exception {
        UserContactInfo userContactInfo = user.getUserContactInfo();
        user.addFriend(userContactInfo);
        user.removeFriend(userContactInfo);
        assertThat(user.getFriends().size(), is(0));
        user.removeFriend(userContactInfo);
    }

    @Test (expected = IllegalStateException.class)
    public void acceptFriendshipRequest(){
        UserContactInfo userContactInfo = user.getUserContactInfo();
        user.getFriendshipRequests().add(userContactInfo);
        user.acceptFriendshipRequest(userContactInfo);
        assertThat(user.getFriends().size(), is(1));
        assertThat(user.getFriendshipRequests().size(), is(0));
        user.acceptFriendshipRequest(userContactInfo);
    }

    @Test (expected = IllegalStateException.class)
    public void rejectFriendshipRequest(){
        UserContactInfo userContactInfo = user.getUserContactInfo();
        user.getFriendshipRequests().add(userContactInfo);
        user.rejectFriendshipRequest(userContactInfo);
        assertThat(user.getFriends().size(), is(0));
        assertThat(user.getFriendshipRequests().size(), is(0));
        user.rejectFriendshipRequest(userContactInfo);
    }

}