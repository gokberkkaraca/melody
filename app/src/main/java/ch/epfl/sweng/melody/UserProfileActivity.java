package ch.epfl.sweng.melody;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.account.LoginStatusHandler;
import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.util.MenuButtons;
import ch.epfl.sweng.melody.user.User;

public class UserProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "ch.epfl.sweng.USERNAME";
    public static final String EXTRA_USERPIC = "ch.epfl.sweng.USERPIC";
    public static final String EXTRA_USERFRIENDS = "ch.epfl.sweng.USERFRIENDS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = findViewById(R.id.username);
        ImageView profilePicView = findViewById(R.id.profilePicView);
        TextView friends = findViewById(R.id.friends);
        Button logout = findViewById(R.id.log_out);
        Button sendFriendRequest = findViewById(R.id.sendFriendRequest);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_USERNAME);
        String userPic = intent.getStringExtra(EXTRA_USERPIC);
        String userFriendsSize = intent.getStringExtra(EXTRA_USERFRIENDS);

        if(userName == null) {
            username.setText(MainActivity.getUser().getDisplayName());
            new GoogleProfilePictureAsync(profilePicView, Uri.parse(MainActivity.getUser().getProfilePhotoUrl())).execute();
            friends.setText(MainActivity.getUser().getFriendsSize());
            sendFriendRequest.setVisibility(View.GONE);
        } else {
            username.setText(userName);
            new GoogleProfilePictureAsync(profilePicView, Uri.parse(userPic)).execute();
            friends.setText(userFriendsSize);
            logout.setVisibility(View.GONE);
        }

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








