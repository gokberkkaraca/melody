package ch.epfl.sweng.melody.database;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.User;


public class DatabaseHandler {
    private static final String FIREBASE_DATABASE_URL = "https://test-84cb3.firebaseio.com/";
    private static final String FIREBASE_STORAGE_URL = "gs://test-84cb3.appspot.com";
    private static final String STORAGE_IMAGES_PATH = "resources/";
    private static final String DATABASE_MEMORIES_PATH = "memories";
    private static final String DATABASE_USERS_PATH = "users";
    private static final String DATABASE_TAGS_PATH = "tags";
    private static final String DATABASE_COMMENTS_PATH = "comments";
    private static final String DATABASE_FRIENDSHIP_REQUESTS_PATH = "friendshipRequests";
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference();
    private static final StorageReference storageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference();

    /**
     * user related database methods
     */

    public static void uploadUser(User user) {
        databaseReference.child(DATABASE_USERS_PATH).child(user.getId()).setValue(user);
    }

    public static void getUser(String userId, ValueEventListener vel) {
        databaseReference.child(DATABASE_USERS_PATH).child(userId).addValueEventListener(vel);
    }

    public static void getUserFriendRequest(String userId, ChildEventListener childEventListener) {
        databaseReference.child(DATABASE_USERS_PATH).child(userId).child(DATABASE_FRIENDSHIP_REQUESTS_PATH).addChildEventListener(childEventListener);
    }

    public static void removeUserFriendRequestListener(String userId,ChildEventListener childEventListener){
        databaseReference.child(DATABASE_USERS_PATH).child(userId).child(DATABASE_FRIENDSHIP_REQUESTS_PATH).removeEventListener(childEventListener);
    }

    /**
     * Memories related database methods
     */

    public static void getMemory(String id, ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(id).addValueEventListener(vel);
    }

    public static void getAllMemories(ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).addValueEventListener(vel);
    }

    public static void removeAllMemoriesListener(ValueEventListener valueEventListener) {
        if (valueEventListener != null) {
            databaseReference.child(DATABASE_MEMORIES_PATH).removeEventListener(valueEventListener);
        }
    }

    public static void getAllMemoriesWithSingleListener(ValueEventListener vel) {
        databaseReference.child(DATABASE_MEMORIES_PATH).addListenerForSingleValueEvent(vel);
    }

    public static void getLatestMemory(ValueEventListener valueEventListener) {
        databaseReference.child(DATABASE_MEMORIES_PATH).limitToFirst(1).addValueEventListener(valueEventListener);
    }

    public static void removeLatestMemoryListener(ValueEventListener valueEventListener) {
        if (valueEventListener != null) {
            databaseReference.child(DATABASE_MEMORIES_PATH).limitToFirst(1).removeEventListener(valueEventListener);
        }
    }

    public static void uploadMemory(Memory memory) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(memory.getId()).setValue(memory.upload());
    }

    public static void addTag(String tag) {
        databaseReference.child(DATABASE_TAGS_PATH).child(tag).setValue(tag);
    }

    public static void getAllTags(ValueEventListener vel) {
        databaseReference.child(DATABASE_TAGS_PATH).addValueEventListener(vel);
    }

    public static void addComment(String memoryId, ch.epfl.sweng.melody.memory.Comment comment) {
        databaseReference.child(DATABASE_MEMORIES_PATH).child(memoryId).child(DATABASE_COMMENTS_PATH).push().setValue(comment);
    }

//    public static void getComments(String memoryId, ValueEventListener vel){
//        databaseReference.child(DATABASE_MEMORIES_PATH).child(memoryId).child("comments").addValueEventListener(vel);
//    }

    public static void uploadResource(Uri uri, Context context,
                                      OnSuccessListener onSuccessListener,
                                      OnFailureListener onFailureListener,
                                      OnProgressListener onProgressListener) {
        storageReference.child(STORAGE_IMAGES_PATH + System.currentTimeMillis() + "." + getResourceExtension(uri, context))
                .putFile(uri)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener)
                .addOnProgressListener(onProgressListener);
    }

    private static String getResourceExtension(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /*
    public static void newFriendshipRequest(User sender, User receiver) {
        receiver.getFriendshipRequests().add(sender.getUserContactInfo());
        uploadUser(receiver);
    }*/
}