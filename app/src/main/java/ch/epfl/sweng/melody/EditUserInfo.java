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


        displayName.setHint(user.getDisplayName());
        displayName.setTextColor(Color.GRAY);
        user.setDisplayName(displayName.getText().toString());
    }


    public void changeBackgroundImage () {}

    public void changeProfileImage () {}

    public void changeUserProfile (View view) {
        String name = displayName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            user.setDisplayName(displayName.getText().toString());
        }

        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
