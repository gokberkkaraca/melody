package ch.epfl.sweng.melody;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.account.LoginStatusHandler;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.database.OnGetDataListener;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class UserProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "ch.epfl.sweng.USERID";

    private static User currentUser;
    private Boolean isMyself = true;

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
        String userId = intent.getStringExtra(EXTRA_USER_ID);

        getUserFromServer(userId);
    }

    private void getUserFromServer(String userId) {
        if (userId == null || userId.equals(MainActivity.getUser().getId())) {
            currentUser = MainActivity.getUser();
            prepareActivityWithUser();
        } else {
            isMyself = false;
            DatabaseHandler.getUserFromId(userId, new OnGetDataListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    prepareActivityWithUser();
                }

                @Override
                public void onFailed(DatabaseError databaseError) {
                }
            });
        }
    }

    private void prepareActivityWithUser() {
        ((TextView) findViewById(R.id.username)).setText(currentUser.getDisplayName());
        ((TextView) findViewById(R.id.friends)).setText(currentUser.getFriendsSize());
        new GoogleProfilePictureAsync((ImageView) findViewById(R.id.profilePicView), Uri.parse(currentUser.getProfilePhotoUrl())).execute();

        if (!isMyself) {
            if (MainActivity.getUser().isFriendWith(currentUser)) {
                findViewById(R.id.removeFriend).setVisibility(View.VISIBLE);
            } else if (MainActivity.getUser().sentFriendshipRequestTo(currentUser)) {
                findViewById(R.id.removeFriendShipRequest).setVisibility(View.VISIBLE);
            } else if (MainActivity.getUser().gotFriendshipRequestFrom(currentUser)) {
                findViewById(R.id.confirmFriendRequest).setVisibility(View.VISIBLE);
                findViewById(R.id.refuseFriendRequest).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
            }
        } else {
            findViewById(R.id.log_out).setVisibility(View.VISIBLE);
        }
    }

    public void removeFriend(View v) {
        MainActivity.getUser().removeFriend(currentUser);
        currentUser.removeFriend(MainActivity.getUser());
        DatabaseHandler.changeFriendsOfUser(MainActivity.getUser().getId(), MainActivity.getUser().getFriends());
        DatabaseHandler.changeFriendsOfUser(currentUser.getId(), currentUser.getFriends());
        findViewById(R.id.removeFriend).setVisibility(View.GONE);
        findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
    }

    public void removeFriendRequest(View v) {
        currentUser.rejectFriendshipRequest(MainActivity.getUser());
        DatabaseHandler.changeFriendsRequestsOfUser(currentUser.getId(), currentUser.getFriendshipRequests());
        findViewById(R.id.removeFriendShipRequest).setVisibility(View.GONE);
        findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
    }

    public void refuseFriendRequest(View v) {
        MainActivity.getUser().rejectFriendshipRequest(currentUser);
        DatabaseHandler.changeFriendsRequestsOfUser(MainActivity.getUser().getId(), MainActivity.getUser().getFriendshipRequests());
        findViewById(R.id.refuseFriendRequest).setVisibility(View.GONE);
        findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
    }

    public void confirmFriendRequest(View v) {
        currentUser.addFriend(MainActivity.getUser());
        MainActivity.getUser().addFriend(currentUser);
        MainActivity.getUser().rejectFriendshipRequest(currentUser);
        DatabaseHandler.changeFriendsRequestsOfUser(MainActivity.getUser().getId(), MainActivity.getUser().getFriendshipRequests());
        DatabaseHandler.changeFriendsOfUser(currentUser.getId(), currentUser.getFriends());
        DatabaseHandler.changeFriendsOfUser(MainActivity.getUser().getId(), MainActivity.getUser().getFriends());
        findViewById(R.id.confirmFriendRequest).setVisibility(View.GONE);
        //findViewById(R.id.youAreFriends).setVisibility(View.VISIBLE);
        findViewById(R.id.removeFriend).setVisibility(View.VISIBLE);
    }

    public void sendFriendRequest(View v) {
        currentUser.addFriendshipRequest(MainActivity.getUser());
        DatabaseHandler.changeFriendsRequestsOfUser(currentUser.getId(), currentUser.getFriendshipRequests());
        findViewById(R.id.sendFriendRequest).setVisibility(View.GONE);
        findViewById(R.id.removeFriendShipRequest).setVisibility(View.VISIBLE);
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








