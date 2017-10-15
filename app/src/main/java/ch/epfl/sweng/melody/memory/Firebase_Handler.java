package ch.epfl.sweng.melody.memory;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase_Handler {
    DatabaseReference database = FirebaseDatabase.getInstance("https://fir-melody.firebaseio.com/").getReference();

    public void AddUserToDatabase(User us){
        database.child("users").child(us.getId()).setValue(us);
    }
}