package ch.epfl.sweng.melody.user;

import android.app.Activity;
import android.content.Intent;

import ch.epfl.sweng.melody.LoginActivity;

public class UserInfoHandler {
    public static User USER_INFO = null;

    public static void checkUserExist(Activity activity){
        if(USER_INFO==null){
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }
}