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

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_INFO);

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
}








