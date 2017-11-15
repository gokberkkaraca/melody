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
        Intent intent = new Intent(context, PublicMemoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void goToMapActivity(Context context, User user) {
        Intent intent = new Intent(context, ShowMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void goToNotificationActivity(Context context, User user) {
        // TODO this method will be linked to map page when it is implemented
    }

    public static void goToUserProfileActivity(Context context, User user) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void goToCreateMemoryActivity(Context context, User user) {
        Intent intent = new Intent(context, CreateMemoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
