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
import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.util.MenuButtons;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = findViewById(R.id.username);
        ImageView profilePicView = findViewById(R.id.profilePicView);

        username.setText(MainActivity.getUser().getDisplayName());
        new GoogleProfilePictureAsync(profilePicView, Uri.parse(MainActivity.getUser().getProfilePhotoUrl())).execute();
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    public void logOut(View view) {
        LoginStatusHandler.clearUserId(this);
        stopService(new Intent(this, FirebaseBackgroundService.class));
        stopService(new Intent(this, LocationService.class));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
        MenuButtons.goToUserProfileActivity(this);
    }
}








