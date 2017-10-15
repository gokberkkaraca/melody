package ch.epfl.sweng.melody.memory;

import java.util.Date;
import java.util.List;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by karunya on 10/13/17.
 */

public class User {
    public String username;
    private String id;
//    private String password;

    //  Google Account
    private GoogleSignInAccount account;

    //  User Info Variables
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phone;
    private Date birthday;
    private String home;

    private List<Memory> memories;
    private List<User> friends;
    private List<User> followers;
    private List<User> following;

    public User(GoogleSignInAccount login, String gender, String phone, Date birthday, String home){
//      Get information from google account
        this.account = login;
        this.id = account.getId();
        this.firstName = account.getGivenName();
        this.lastName = account.getFamilyName();
        this.username = account.getDisplayName();
//        this.password = password
        this.email = account.getEmail();

        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        this.home = home;
        this.memories = null;
        this.friends = null;
        this.followers = null;
        this.following = null;
    }

    //  Getter & Setters
    public String getId (){
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getGender(){
        return gender;
    }

    public String getEmail(){
        return email;
    }

//    public void setEmail(String email){
//        this.email = email;
//    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public Date getBirthday(){
        return birthday;
    }

    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    public String getHome(){
        return home;
    }

    public void setHome(String home){
        this.home = home;
    }
}