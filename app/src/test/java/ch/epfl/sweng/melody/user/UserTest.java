package ch.epfl.sweng.melody.user;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.melody.memory.Memory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";
    private User user, otherUser;

    @Before
    public void setUp () throws Exception {

        final FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn("Jiacheng Xu");
        when(firebaseUser.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
        user = new User(firebaseUser);

        otherUser = new User(firebaseUser);
    }

    @Test
    public void getId () throws Exception {
        assertEquals(user.getId(), "jiacheng,xu@epfl,ch");
    }

    @Test
    public void getDisplayName () throws Exception {
        assertEquals(user.getDisplayName(), "Jiacheng Xu");
    }

    @Test
    public void getEmail () throws Exception {
        assertEquals(user.getEmail(), "jiacheng.xu@epfl.ch");
    }

    @Test
    public void getMemories () throws Exception {
        assertEquals(user.getMemories(), new ArrayList<Memory>());
    }

    @Test
    public void getFriends () throws Exception {
        assertEquals(user.getFriends(), new HashMap<String, UserContactInfo>());
    }

    @Test
    public void getFriendsSize () throws Exception {
        assertEquals(user.getFriendsSize(), "0");
    }

    @Test
    public void getListFriends () throws Exception {
        assertEquals(user.getListFriends(), new ArrayList<UserContactInfo>());
    }

    @Test
    public void getFriendshipRequests () throws Exception {
        assertEquals(user.getFriendshipRequests(), new HashMap<String, UserContactInfo>());
    }

    @Test
    public void getFriendshipListRequests () throws Exception {
        assertEquals(user.getFriendshipListRequests(), new ArrayList<UserContactInfo>());
    }

    @Test
    public void getFollowers () throws Exception {
        assertEquals(user.getFollowers(), new ArrayList<User>());
    }

    @Test
    public void getFollowings () throws Exception {
        assertEquals(user.getFollowings(), new ArrayList<User>());
    }

    @Test
    public void getProfilePhotoUrl () throws Exception {
        assertEquals(user.getProfilePhotoUrl(), defaultProfilePhotoUrl);
    }

    @Test
    public void getDefaultProfilePhotoURL () throws Exception {
        assertEquals(user.getDefaultProfilePhotoUrl(), defaultProfilePhotoUrl);
    }

    @Test
    public void getThemeColor () throws Exception {
        assertEquals(user.getThemeColor(), User.ThemeColor.BLACK);
    }

    @Test
    public void getMinRadius () throws Exception {
        assertEquals(user.getMinRadius(), 1);
    }

    @Test
    public void getMaxRadius () throws Exception {
        assertEquals(user.getMaxRadius(), 100);
    }

    @Test
    public void getNotificationsOn () throws Exception {
        assertEquals(user.getNotificationsOn(), true);
    }

    @Test
    public void setThemeColor () throws Exception {
        user.setThemeColor(User.ThemeColor.RED);
        assertEquals(user.getThemeColor(), User.ThemeColor.RED);
    }

    @Test
    public void setMinRadius () throws Exception {
        user.setMinRadius(2);
        assertEquals(user.getMinRadius(), 2);
    }

    @Test
    public void setMaxRadius () throws Exception {
        user.setMaxRadius(50);
        assertEquals(user.getMaxRadius(), 50);
    }

    @Test
    public void setNotificationsOn () throws Exception {
        user.setNotificationsOn(false);
        assertEquals(user.getNotificationsOn(), false);
    }

    @Test
    public void getBiography () {
        assertEquals(user.getBiography(), "");
    }

    @Test
    public void setBiograhy () {
        String bio = "A superhero";
        user.setBiograhy(bio);
        assertEquals(user.getBiography(), bio);
    }

    @Test
    public void setDisplayName () {
        String name = "Pikachu";
        user.setDisplayName(name);
        assertEquals(user.getDisplayName(), name);
    }

    @Test
    public void emptyConstructor () throws Exception {
        assertNotNull(new User());
    }

    @Test
    public void getUserContactInfo () throws Exception {
        UserContactInfo userContactInfo = user.getUserContactInfo();
        UserContactInfo userContactInfo1 = user.getUserContactInfo();
        assertTrue(userContactInfo.equals(userContactInfo1));
        assertThat(userContactInfo.getDisplayName(), is(user.getDisplayName()));
        assertThat(userContactInfo.getEmail(), is(user.getEmail()));
        assertThat(userContactInfo.getProfilePhotoUrl(), is(user.getProfilePhotoUrl()));
        assertThat(userContactInfo.getUserId(), is(user.getId()));
    }

    @Test(expected = IllegalStateException.class)
    public void addFriend() throws Exception {
        user.addFriend(otherUser);
        assertThat(user.getFriends().size(), is(1));
        user.addFriend(otherUser);
    }

    @Test(expected = IllegalStateException.class)
    public void addFriendshipRequest() throws Exception {
        user.addFriendshipRequest(otherUser);
        assertThat(user.getFriendshipListRequests().size(), is(1));
        user.addFriendshipRequest(otherUser);
    }

    @Test(expected = IllegalStateException.class)
    public void removeFriend () throws Exception {
        user.addFriend(otherUser);
        user.removeFriend(otherUser);
        assertThat(user.getFriends().size(), is(0));
        user.removeFriend(otherUser);
    }

    @Test(expected = IllegalStateException.class)
    public void acceptFriendshipRequest () {
        user.getFriendshipRequests().put(otherUser.getId(), otherUser.getUserContactInfo());
        user.acceptFriendshipRequest(otherUser);
        assertThat(user.getFriends().size(), is(1));
        assertThat(user.getFriendshipRequests().size(), is(0));
        user.acceptFriendshipRequest(otherUser);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectFriendshipRequest () {
        user.getFriendshipRequests().put(otherUser.getId(), otherUser.getUserContactInfo());
        user.rejectFriendshipRequest(otherUser);
        assertThat(user.getFriends().size(), is(0));
        assertThat(user.getFriendshipRequests().size(), is(0));
        user.rejectFriendshipRequest(otherUser);
    }

}