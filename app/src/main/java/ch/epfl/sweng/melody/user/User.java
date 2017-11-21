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
    private List<User> friends;
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
        followers = new ArrayList<>();
        followings = new ArrayList<>();
    }

    // Empty constructor is needed for database connection
    public User() {
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

    public List<User> getFriends() {
        return friends;
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

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(User.class) && this.id.equals(((User) obj).getId());
    }
}