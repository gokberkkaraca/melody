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

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_INFO);

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(user.getDisplayName());

        ImageView profilePicView = (ImageView) findViewById(R.id.profilePicView);
        new GoogleProfilePictureAsync(profilePicView, Uri.parse(user.getProfilePhotoUrl())).execute();
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
        MenuButtons.goToCreateMemoryActivity(this, user);
    }

    public void goToPublicMemoryActivity(View view) {
        MenuButtons.goToPublicMemoryActivity(this, user);
    }

    public void goToMapActivity(View view) {
        MenuButtons.goToMapActivity(this, user);
    }

    public void goToNotification(View view) {
        MenuButtons.goToNotificationActivity(this, user);
    }

    public void goToUser(View view) {
        MenuButtons.goToUserProfileActivity(this, user);
    }
}








