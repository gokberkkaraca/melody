package ch.epfl.sweng.melody.database;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.User;


public class DatabaseHandler {

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
    private final String FIREBASE_DATABASE_URL = "https://fir-melody.firebaseio.com/";
    private final String FIREBASE_STORAGE_URL = "gs://firebase-melody.appspot.com";
    private final String STORAGE_IMAGES_PATH = "images/";
    private final String DATABASE_MEMORIES_PATH = "memories";
    private final String DATABASE_USERS_PATH = "users";
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public DatabaseHandler(){
        databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference();
        storageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference();
    }

    public void addUser(User user){
        databaseReference.child("users").child(user.getId()).setValue(user);
    }

    public void getUserInfo(String userId, ValueEventListener vel){
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(vel);
    }

    public void uploadMemory(Memory memory, Context context){
        databaseReference.child("memories").setValue(memory);
    }

    private void uploadImage(Uri uri, Context context){
        storageReference.child(STORAGE_IMAGES_PATH + System.currentTimeMillis()+ "."+getImageExtenson(uri,context)).putFile(uri);
    }

    private String getImageExtenson(Uri uri, Context context){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}