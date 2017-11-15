package ch.epfl.sweng.melody.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ch.epfl.sweng.melody.CreateMemoryActivity;
import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.PublicMemoryActivity;
import ch.epfl.sweng.melody.ShowMapActivity;
import ch.epfl.sweng.melody.UserProfileActivity;
import ch.epfl.sweng.melody.user.User;

public class MenuButtons {
    public static void goToPublicMemoryActivity(Context context, User user) {
        goToActivity(context, user,PublicMemoryActivity.class);
    }

    public static void goToMapActivity(Context context, User user) {
        goToActivity(context,user,ShowMapActivity.class);
    }

    public static void goToNotificationActivity(Context context, User user) {
        // TODO this method will be linked to map page when it is implemented
    }

    public static void goToUserProfileActivity(Context context, User user) {
        goToActivity(context, user,UserProfileActivity.class);
    }

    public static void goToCreateMemoryActivity(Context context, User user) {
        goToActivity(context,user,CreateMemoryActivity.class);
    }

    private static void goToActivity(Context context,User user,Class cls){
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
