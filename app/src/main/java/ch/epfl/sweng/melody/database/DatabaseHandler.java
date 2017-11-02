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
    private static final String STORAGE_IMAGES_PATH = "resources/";
    private static final String DATABASE_MEMORIES_PATH = "memories";
    private static final String DATABASE_USERS_PATH = "users";
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference();
    private static final StorageReference storageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference();

    public static void addUser(User user) {
        databaseReference.child(DATABASE_USERS_PATH).child(user.getId()).setValue(user);
    }

    public static void getUserInfo(String userId, ValueEventListener vel) {
        databaseReference.child(DATABASE_USERS_PATH).child(userId).addValueEventListener(vel);
    }

    public static void getAllMemories(ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).addValueEventListener(vel);
    }

    public static void getMemory(String id, ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(id).addValueEventListener(vel);
    }

    public static void getLatestMemory(ValueEventListener valueEventListener){
        databaseReference.child(DATABASE_MEMORIES_PATH).limitToFirst(1).addValueEventListener(valueEventListener);
    }


    public static void uploadMemory(Memory memory) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(memory.getId()).setValue(memory.upload());
    }

    public static void uploadResource(Uri uri, Context context,
                                      OnSuccessListener onSuccessListener,
                                      OnFailureListener onFailureListener,
                                      OnProgressListener onProgressListener) {
        storageReference.child(STORAGE_IMAGES_PATH + System.currentTimeMillis() + "." + getResourceExtenson(uri, context))
                .putFile(uri)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener)
                .addOnProgressListener(onProgressListener);
    }

    private static String getResourceExtenson(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}