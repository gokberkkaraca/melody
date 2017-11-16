package ch.epfl.sweng.melody;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.account.LoginStatusHandler;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = findViewById(R.id.username);
        username.setText(MainActivity.user.getDisplayName());

        ImageView profilePicView = findViewById(R.id.profilePicView);
        new GoogleProfilePictureAsync(profilePicView, Uri.parse(MainActivity.user.getProfilePhotoUrl())).execute();
    }

    public void logOut(View view) {
        LoginStatusHandler.clearUserId(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /*************************************************
     ******************* Menu Buttons ****************
     *************************************************/
    public void goToCreateMemoryActivity(View view) {
        MenuButtons.goToCreateMemoryActivity(this);
    }

    public void goToPublicMemoryActivity(View view) {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    public void goToMapActivity(View view) {
        MenuButtons.goToMapActivity(this);
    }

    public void goToNotification(View view) {
        MenuButtons.goToNotificationActivity(this);
    }

    public void goToUser(View view) {
        MenuButtons.goToUserProfileActivity(this)
    }
}








