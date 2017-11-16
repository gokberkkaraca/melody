package ch.epfl.sweng.melody.util;

import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.melody.CreateMemoryActivity;
import ch.epfl.sweng.melody.PublicMemoryActivity;
import ch.epfl.sweng.melody.ShowMapActivity;
import ch.epfl.sweng.melody.UserProfileActivity;

public class MenuButtons {
    public static void goToPublicMemoryActivity(Context context) {
        goToActivity(context, PublicMemoryActivity.class);
    }

    public static void goToMapActivity(Context context) {
        goToActivity(context, ShowMapActivity.class);
    }

    public static void goToNotificationActivity(Context context) {
        // TODO this method will be linked to map page when it is implemented
    }

    public static void goToUserProfileActivity(Context context) {
        goToActivity(context, UserProfileActivity.class);
    }

    public static void goToCreateMemoryActivity(Context context) {
        goToActivity(context, CreateMemoryActivity.class);
    }

    private static void goToActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
}
