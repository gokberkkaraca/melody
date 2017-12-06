package ch.epfl.sweng.melody;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.DialogUtils;

public class EditUserInfo extends AppCompatActivity {
    User user;
    EditText displayName;
    EditText userBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        setTitle("");
        Toolbar editToorbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(editToorbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = MainActivity.getUser();
        displayName = findViewById(R.id.change_display_name);
        userBio = findViewById(R.id.change_user_bio);

        displayName.setText(user.getDisplayName());
        displayName.setTextColor(Color.GRAY);
        userBio.setText(user.getBiography());
    }


    public void changeBackgroundImage () {}

    public void changeProfileImage () {}

    public void changeUserProfile (View view) {
        String name = displayName.getText().toString();
        String bio = userBio.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            user.setDisplayName(name);
        }
        if (!TextUtils.isEmpty(bio)) {
            if(bio.length() > 500){
                Toast.makeText(getApplicationContext(), R.string.bio_too_long, Toast.LENGTH_SHORT).show();
                return;
            }else {
                user.setBiograhy(bio);
            }
        }

        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
