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
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class UserProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "ch.epfl.sweng.USERID";

    private static User currentUser;
    private Boolean isMyself = true;
    private TextView edit;
    private TextView bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = findViewById(R.id.username);
        ImageView profilePicView = findViewById(R.id.profilePicView);
        TextView friends = findViewById(R.id.friends);
        Button logout = findViewById(R.id.log_out);
        Button sendFriendRequest = findViewById(R.id.sendFriendRequest);
        edit = findViewById(R.id.edit_userInfo);
        bio = findViewById(R.id.user_bio);

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
            edit.setVisibility(View.GONE);
            DatabaseHandler.getUser(userId, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    prepareActivityWithUser();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void prepareActivityWithUser() {
        bio.setText(currentUser.getBiography());
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
        DatabaseHandler.uploadUser(MainActivity.getUser());
        DatabaseHandler.uploadUser(currentUser);
        findViewById(R.id.removeFriend).setVisibility(View.GONE);
        findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
    }

    public void removeFriendRequest(View v) {
        currentUser.rejectFriendshipRequest(MainActivity.getUser());
        DatabaseHandler.uploadUser(currentUser);
        findViewById(R.id.removeFriendShipRequest).setVisibility(View.GONE);
        findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
    }

    public void refuseFriendRequest(View v) {
        MainActivity.getUser().rejectFriendshipRequest(currentUser);
        DatabaseHandler.uploadUser(MainActivity.getUser());
        findViewById(R.id.refuseFriendRequest).setVisibility(View.GONE);
        findViewById(R.id.sendFriendRequest).setVisibility(View.VISIBLE);
    }

    public void confirmFriendRequest(View v) {
        currentUser.addFriend(MainActivity.getUser());
        MainActivity.getUser().addFriend(currentUser);
        MainActivity.getUser().rejectFriendshipRequest(currentUser);
        DatabaseHandler.uploadUser(currentUser);
        DatabaseHandler.uploadUser(MainActivity.getUser());
        findViewById(R.id.confirmFriendRequest).setVisibility(View.GONE);
        //findViewById(R.id.youAreFriends).setVisibility(View.VISIBLE);
        findViewById(R.id.removeFriend).setVisibility(View.VISIBLE);
    }

    public void sendFriendRequest(View v) {
        currentUser.addFriendshipRequest(MainActivity.getUser());
        DatabaseHandler.uploadUser(currentUser);
        findViewById(R.id.sendFriendRequest).setVisibility(View.GONE);
        findViewById(R.id.removeFriendShipRequest).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    public void logOut(View view) {
        MainActivity.getFirebaseAuthInstance().signOut();
        stopService(new Intent(this, FirebaseBackgroundService.class));
        stopService(new Intent(this, LocationService.class));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void editUserInfo (View view){
        Intent intent = new Intent(this, EditUserInfoActivity.class);
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
        MenuButtons.goToUserProfileActivity(this);
    }
}








