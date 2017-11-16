package ch.epfl.sweng.melody;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.account.LoginStatusHandler;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.service.FirebaseBackgroundService;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class MainActivity extends AppCompatActivity {

    public static final String USER_INFO = "USER";

    private static User user;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final int timer = 250;
        super.onCreate(savedInstanceState);
        startService(new Intent(this, FirebaseBackgroundService.class));
        setContentView(R.layout.activity_main);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String userId = LoginStatusHandler.getUserId(MainActivity.this);

                if (userId.length() == 0) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    DatabaseHandler.getUserInfo(userId, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            MenuButtons.goToPublicMemoryActivity(MainActivity.this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }, timer);
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MainActivity.user = user;
    }
}
