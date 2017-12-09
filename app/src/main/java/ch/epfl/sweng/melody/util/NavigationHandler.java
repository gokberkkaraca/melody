package ch.epfl.sweng.melody.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.melody.CreateMemoryActivity;
import ch.epfl.sweng.melody.EditUserInfoActivity;
import ch.epfl.sweng.melody.FriendListActivity;
import ch.epfl.sweng.melody.LoginActivity;
import ch.epfl.sweng.melody.PublicMemoryActivity;
import ch.epfl.sweng.melody.ResetPasswordActivity;
import ch.epfl.sweng.melody.SettingsActivity;
import ch.epfl.sweng.melody.ShowMapActivity;
import ch.epfl.sweng.melody.SignUpActivity;
import ch.epfl.sweng.melody.UserProfileActivity;
import ch.epfl.sweng.melody.memory.Memory;

import static ch.epfl.sweng.melody.PublicMemoryActivity.EXTRA_GOING_TO_USER_LIST;
import static ch.epfl.sweng.melody.UserProfileActivity.EXTRA_USER_ID;

public class NavigationHandler {
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

    public static void goToUserProfileActivityFromUserMemory(Context context, Memory memory){
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(EXTRA_USER_ID, memory.getUser().getId());
        context.startActivity(intent);
    }

    public static void goToResetPasswordActivity(Context context){
        goToActivity(context, ResetPasswordActivity.class);
    }

    public static void goToLogInActivity(Context context){
        goToActivity(context, LoginActivity.class);
    }

    public static void goToSignUpActivity(Context context){
        goToActivity(context, SignUpActivity.class);
    }

    public static void goToFriendListActivity(Context context,String listType){
        Intent intent = new Intent(context, FriendListActivity.class);
        intent.putExtra(EXTRA_GOING_TO_USER_LIST, listType);
        context.startActivity(intent);
    }

    public static void goToSettingsActivity(Context context){
        goToActivity(context, SettingsActivity.class);
    }

    public static void goToEditUserInfoActivity(Context context){
        goToActivity(context ,EditUserInfoActivity.class);
    }

    private static void goToActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
