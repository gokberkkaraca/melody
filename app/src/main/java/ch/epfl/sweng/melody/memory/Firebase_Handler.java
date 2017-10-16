package ch.epfl.sweng.melody.memory;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Firebase_Handler {
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
    DatabaseReference database = FirebaseDatabase.getInstance("https://fir-melody.firebaseio.com/").getReference();

    public void addUserToDatabase(User us){
        database.child("users").child(us.getId()).setValue(us);
    }

    public void getUserInfo(String id,ValueEventListener vel){
        database.child("users").child(id).addListenerForSingleValueEvent(vel);
    }
}