package ch.epfl.sweng.melody.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.melody.memory.Memory;

public class User implements Serializable {
    //  User Info Variables
    private String username;
    private String id;
    private String firstName;
    private String lastName;
    private String profilePhotoUrl;

    private String displayName;
    private String gender;
    private String email;
    private String phone;
    private Date birthday;
    private String home;

    private List<Memory> memories;
    private List<User> friends;
    private List<User> followers;
    private List<User> followings;

    public User(GoogleSignInAccount googleSignInAccount) {
        if (googleSignInAccount != null) {
            this.id = googleSignInAccount.getId();
            this.firstName = googleSignInAccount.getGivenName();
            this.lastName = googleSignInAccount.getFamilyName();
            this.displayName = googleSignInAccount.getDisplayName();
            this.email = googleSignInAccount.getEmail();
            assert email != null;
            this.username = email.substring(0, email.indexOf('@'));

            if (googleSignInAccount.getPhotoUrl() != null)
                this.profilePhotoUrl = googleSignInAccount.getPhotoUrl().toString();
            else
                this.profilePhotoUrl = "http://www.purdue.edu/gradschool/gspd/images/ppl/empty.png";
        }

        memories = new ArrayList<>();
        friends = new ArrayList<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
    }

    // Empty constructor is needed for database connection
    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
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
}