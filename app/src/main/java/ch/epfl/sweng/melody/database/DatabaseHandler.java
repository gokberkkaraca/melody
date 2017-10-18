package ch.epfl.sweng.melody.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.user.User;


public class DatabaseHandler {

    private static String databaseURL = "https://fir-melody.firebaseio.com/";

    /*
    * Example how to use this class (in PublicMemoryActivity) :
    * Firebase_Handler fh = new Firebase_Handler();
        fh.addUserToDatabase(new User(LoginActivity.GOOGLE_ACCOUNT,"m","9191919191",new Date(),"lausanne"));

        fh.getUserInfo(LoginActivity.GOOGLE_ACCOUNT.getId(),new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tmp = dataSnapshot.getValue(User.class);
                addTextMemory(tmp.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
      });
    *
    *
    *
    *
    * */

    private static DatabaseReference database = FirebaseDatabase.getInstance(databaseURL).getReference();

    public static void addUser(User user){
        database.child("users").child(user.getId()).setValue(user);
    }

    public static void getUserInfo(String userId, ValueEventListener vel){
        database.child("users").child(userId).addListenerForSingleValueEvent(vel);
    }
}