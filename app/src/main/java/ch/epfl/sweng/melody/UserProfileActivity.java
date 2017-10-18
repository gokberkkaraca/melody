package ch.epfl.sweng.melody;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.user.User;

public class UserProfileActivity extends AppCompatActivity {

    // TODO User object couldn't be created
    //User user = (User) getIntent().getExtras().getSerializable("USER");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView username = (TextView) findViewById(R.id.username);
        //username.setText(user.getDisplayName());

        ImageView profilePicView = (ImageView) findViewById(R.id.profilePicView);
        //new GoogleProfilePictureAsync(profilePicView, user.getProfilePhotoUrl()).execute();
    }
}








