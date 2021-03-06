package ch.epfl.sweng.melody;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;
import ch.epfl.sweng.melody.util.NavigationHandler;

public class MainActivity extends AppCompatActivity {

    public static final String USER_INFO = "USER";

    private static User user;
    private static FirebaseAuth firebaseAuth = null;
    private final Handler mHandler = new Handler();

    public static FirebaseAuth getFirebaseAuthInstance() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MainActivity.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int timer = 250;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getFirebaseAuthInstance().getCurrentUser() == null) {
                    NavigationHandler.goToLogInActivity(MainActivity.this);
                } else {
                    user = new User(getFirebaseAuthInstance().getCurrentUser());
                    DatabaseHandler.getUserWithSingleListener(MainActivity.getUser().getId(), new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            NavigationHandler.goToPublicMemoryActivity(MainActivity.this);
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
