package ch.epfl.sweng.melody.user;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.melody.memory.Memory;

public class User implements Serializable {

    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";
    //  User Info Variables
    private String id;
    private String profilePhotoUrl;
    private String displayName;
    private String email;
    private List<Memory> memories;
    private Map<String, UserContactInfo> friends;
    private Map<String, UserContactInfo> friendshipRequests;
    private List<User> followers;
    private List<User> followings;
    private ThemeColor themeColor;
    private int minRadius;
    private int maxRadius;
    private boolean notificationsOn;

    public User(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            this.id = encodeEmailForId(firebaseUser.getEmail());
            this.displayName = firebaseUser.getDisplayName();
            this.email = firebaseUser.getEmail();
            profilePhotoUrl = defaultProfilePhotoUrl;
            if (firebaseUser.getPhotoUrl() != null)
                this.profilePhotoUrl = firebaseUser.getPhotoUrl().toString();
        }
        memories = new ArrayList<>();
        friends = new HashMap<>();
        friendshipRequests = new HashMap<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();

        themeColor = ThemeColor.BLACK;
        minRadius = 1;
        maxRadius = 100;
        notificationsOn = true;
    }

    // Empty constructor is needed for database connection, for friend too
    public User() {
        memories = new ArrayList<>();
        friends = new HashMap<>();
        friendshipRequests = new HashMap<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
        themeColor = ThemeColor.BLACK;
        minRadius = 1;
        maxRadius = 100;
        notificationsOn = true;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getFriendsSize() {
        return (friends == null) ? "0" : Integer.toString(friends.size());
    }

    public List<Memory> getMemories() {
        return memories;
    }

    public List<UserContactInfo> getListFriends() {
        return new ArrayList<UserContactInfo>(friends.values());
    }

    public Map<String, UserContactInfo> getFriends() {
        return friends;
    }

    public List<UserContactInfo> getFriendshipListRequests() {
        return new ArrayList<UserContactInfo>(friendshipRequests.values());
    }

    public Map<String, UserContactInfo> getFriendshipRequests() {
        return friendshipRequests;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public String getDefaultProfilePhotoUrl() {
        return defaultProfilePhotoUrl;
    }

    public ThemeColor getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(ThemeColor color) {
        themeColor = color;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int r) {
        minRadius = r;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int r) {
        maxRadius = r;
    }

    public boolean getNotificationsOn() {
        return notificationsOn;
    }

    public void setNotificationsOn(boolean b) {
        notificationsOn = b;
    }

    private String encodeEmailForId(String email) {
        return email.replace('.', ',');
    }

    public boolean isFriendWith(User otherUser) {
        return friends.containsKey(otherUser.getId());
    }

    public boolean sentFriendshipRequestTo(User otherUser) {
        return otherUser.getFriendshipRequests().containsKey(this.getId());
    }

    public boolean gotFriendshipRequestFrom(User otherUser) {
        return friendshipRequests.containsKey(otherUser.getId());
    }

    public void removeFriend(User otherUser) {
        if (friends.containsKey(otherUser.getId())) {
            friends.remove(otherUser.getId());
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is not in friend list");
        }
    }

    public void addFriend(User otherUser) {
        if (!friends.containsKey(otherUser.getId())) {
            friends.put(otherUser.getId(), otherUser.getUserContactInfo());
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is already in friend list");
        }
    }

    public void addFriendshipRequest(User otherUser) {
        if (!friendshipRequests.containsKey(otherUser.getId())) {
            friendshipRequests.put(otherUser.getId(), otherUser.getUserContactInfo());
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is already in friendshipRequest list");
        }
    }

    public UserContactInfo getUserContactInfo() {
        return new UserContactInfo(id, displayName, profilePhotoUrl, email);
    }

    public void acceptFriendshipRequest(User otherUserId) {
        if (friendshipRequests.containsKey(otherUserId.getId())) {
            friendshipRequests.remove(otherUserId.getId());
            addFriend(otherUserId);
        } else {
            throw new IllegalStateException("Friendship request does not exist");
        }
    }

    public void rejectFriendshipRequest(User otherUserId) {
        if (friendshipRequests.containsKey(otherUserId.getId())) {
            friendshipRequests.remove(otherUserId.getId());
        } else {
            throw new IllegalStateException("Friendship request does not exist");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(User.class) && this.id.equals(((User) obj).getId());
    }

    public enum ThemeColor {RED, GREEN, BLUELIGHT, BLUEDARK, BLACK}
}