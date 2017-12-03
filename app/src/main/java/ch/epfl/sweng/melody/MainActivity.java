package ch.epfl.sweng.melody;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class MainActivity extends AppCompatActivity {

    public static final String USER_INFO = "USER";

    private static User user;
    private static FirebaseAuth firebaseAuth = null;
    private final Handler mHandler = new Handler();

    public static FirebaseAuth initializeFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(FirebaseUser firebaseUser) {
        MainActivity.user = new User(firebaseUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int timer = 250;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (initializeFirebaseAuth().getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    user = new User(initializeFirebaseAuth().getCurrentUser());
                    DatabaseHandler.getUser(MainActivity.getUser().getId(), new ValueEventListener() {
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
}
