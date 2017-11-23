package ch.epfl.sweng.melody.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.memory.Memory;

public class User implements Serializable {

    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";

    //  User Info Variables
    private String id;
    private String profilePhotoUrl;

    private String displayName;
    private String email;

    private List<Memory> memories;
    private List<UserContactInfo> friends;
    private List<UserContactInfo> friendshipRequests;
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
        friends = new ArrayList<>();
        friendshipRequests = new ArrayList<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
    }

    // Empty constructor is needed for database connection
    public User() {
        memories = new ArrayList<>();
        friends = new ArrayList<>();
        friendshipRequests = new ArrayList<>();
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

    public List<Memory> getMemories() {
        return memories;
    }

    public List<UserContactInfo> getFriends() {
        return friends;
    }

    public List<UserContactInfo> getFriendshipRequests() {
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

    public void removeFriend(UserContactInfo otherUser) {
        if (friends.contains(otherUser)) {
            friends.remove(otherUser);
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is not in friend list");
        }
    }

    public void addFriend(UserContactInfo otherUser) {
        if (!friends.contains(otherUser)) {
            friends.add(otherUser);
        } else {
            throw new IllegalStateException("User " + otherUser.getDisplayName() + " is already in friend list");
        }
    }

    public UserContactInfo getUserContactInfo() {
        return new UserContactInfo(id, displayName, profilePhotoUrl, email);
    }

    public void acceptFriendshipRequest(UserContactInfo otherUserId) {
        if (friendshipRequests.contains(otherUserId)) {
            friendshipRequests.remove(otherUserId);
            addFriend(otherUserId);
        } else {
            throw new IllegalStateException("Friendship request does not exist");
        }
    }

    public void rejectFriendshipRequest(UserContactInfo otherUserId) {
        if (friendshipRequests.contains(otherUserId)) {
            friendshipRequests.remove(otherUserId);
        } else {
            throw new IllegalStateException("Friendship request does not exist");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(User.class) && this.id.equals(((User) obj).getId());
    }
}