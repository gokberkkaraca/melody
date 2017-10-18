package ch.epfl.sweng.melody;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.melody.memory.GoogleProfilePictureAsync;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(LoginActivity.GOOGLE_ACCOUNT.getDisplayName());

        ImageView profilePicView = (ImageView) findViewById(R.id.profilePicView);
        new GoogleProfilePictureAsync(profilePicView, LoginActivity.GOOGLE_ACCOUNT.getPhotoUrl()).execute();
    }
}








