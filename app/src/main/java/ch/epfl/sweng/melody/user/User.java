package ch.epfl.sweng.melody.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.melody.memory.Memory;

public class User implements Serializable {

    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";

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

    public User(GoogleSignInAccount googleSignInAccount) {
        if (googleSignInAccount != null) {
            this.id = encodeEmailForId(googleSignInAccount.getEmail());
            this.displayName = googleSignInAccount.getDisplayName();
            this.email = googleSignInAccount.getEmail();
            assert email != null;

            profilePhotoUrl = defaultProfilePhotoUrl;
            if (googleSignInAccount.getPhotoUrl() != null)
                this.profilePhotoUrl = googleSignInAccount.getPhotoUrl().toString();
        }

        memories = new ArrayList<>();
        friends = new HashMap<>();
        friendshipRequests = new HashMap<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
    }

    // Empty constructor is needed for database connection
    public User() {
        memories = new ArrayList<>();
        friends = new HashMap<>();
        friendshipRequests = new HashMap<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
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

    public String getFriendsSize() { return (friends == null) ? "0" : Integer.toString(friends.size()); }

    public List<Memory> getMemories() {
        return memories;
    }

    public Map<String, UserContactInfo> getFriends() {
        return friends;
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

    private String encodeEmailForId(String email) {
        return email.replace('.', ',');
    }

    private String decodeEmailForId(String email) {
        return email.replace(',', '.');
    }

    public void removeFriend(UserContactInfo otherUser) {
        if (friends.containsKey(otherUser.getUserId())) {
                friends.remove(otherUser.getUserId());
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is not in friend list");
        }
    }

    public void addFriend(UserContactInfo otherUser) {
        if (!friends.containsKey(otherUser.getUserId())) {
            friends.put(otherUser.getUserId(), otherUser);
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is already in friend list");
        }
    }

    public UserContactInfo getUserContactInfo() {
        return new UserContactInfo(id, displayName, profilePhotoUrl, email);
    }

    public void acceptFriendshipRequest(UserContactInfo otherUserId) {
        if (friendshipRequests.containsKey(otherUserId.getUserId())) {
            friendshipRequests.remove(otherUserId.getUserId());
            addFriend(otherUserId);
        } else {
            throw new IllegalStateException("Friendship request does not exist");
        }
    }

    public void rejectFriendshipRequest(UserContactInfo otherUserId) {
        if (friendshipRequests.containsKey(otherUserId.getUserId())) {
            friendshipRequests.remove(otherUserId.getUserId());
        } else {
            throw new IllegalStateException("Friendship request does not exist");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(User.class) && this.id.equals(((User) obj).getId());
    }
}