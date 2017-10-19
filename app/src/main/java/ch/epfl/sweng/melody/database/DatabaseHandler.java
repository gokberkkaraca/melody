package ch.epfl.sweng.melody.database;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.User;


public class DatabaseHandler {
    private static final String FIREBASE_DATABASE_URL = "https://fir-melody.firebaseio.com/";
    private static final String FIREBASE_STORAGE_URL = "gs://firebase-melody.appspot.com";
    private static final String STORAGE_IMAGES_PATH = "images/";
    private static final String DATABASE_MEMORIES_PATH = "memories";
    private static final String DATABASE_USERS_PATH = "users";
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference();
    private static StorageReference storageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference();

    public static void addUser(User user) {
        databaseReference.child(DATABASE_USERS_PATH).child(user.getId()).setValue(user);
    }

    public static void getUserInfo(String userId, ValueEventListener vel) {
        databaseReference.child(DATABASE_USERS_PATH).child(userId).addListenerForSingleValueEvent(vel);
    }

    public static void getAllMemories(ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).addListenerForSingleValueEvent(vel);
    }

    public static void getMemory(String id,ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(id).addListenerForSingleValueEvent(vel);
    }


    public static void uploadMemory(Memory memory) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(memory.getID().toString()).setValue(memory);
    }

    public static void uploadImage(Uri uri, Context context,
                                   OnSuccessListener onSuccessListener,
                                   OnFailureListener onFailureListener,
                                   OnProgressListener onProgressListener) {
        storageReference.child(STORAGE_IMAGES_PATH + System.currentTimeMillis() + "." + getImageExtenson(uri, context))
                .putFile(uri)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener)
                .addOnProgressListener(onProgressListener);
    }

    private static String getImageExtenson(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

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

}