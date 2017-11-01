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
import ch.epfl.sweng.melody.user.UserInfoHandler;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = (TextView) findViewById(R.id.username);
        assert UserInfoHandler.USER_INFO!=null;
        username.setText(UserInfoHandler.USER_INFO.getDisplayName());

        ImageView profilePicView = (ImageView) findViewById(R.id.profilePicView);
        new GoogleProfilePictureAsync(profilePicView, Uri.parse(UserInfoHandler.USER_INFO.getProfilePhotoUrl())).execute();
    }

    public void logOut(View view) {
        LoginStatusHandler.clearUserId(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToPublicMemory(View view) {
        Intent intent = new Intent(this, PublicMemoryActivity.class);
        startActivity(intent);
    }
}








