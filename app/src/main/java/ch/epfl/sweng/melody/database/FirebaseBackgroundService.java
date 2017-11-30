package ch.epfl.sweng.melody.database;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.RestrictTo;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;
import ch.epfl.sweng.melody.user.UserContactInfo;

/**
 * Created by maxwell on 02.11.17.
 */

public class FirebaseBackgroundService extends Service {
    @RestrictTo(RestrictTo.Scope.TESTS)
    private static boolean isServiceStarted;
    private static ValueEventListener valueEventListenerMemory;
    private static ChildEventListener childEventListenerFriendRequest;
    private long memoryCounter;
    private long friendRequestCounter;
    private long latestMemoryId;
    private int initialLoadFriendRequest;

    public static boolean isServiceStarted() {
        return isServiceStarted;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceStarted = true;
        memoryCounter = 0;
        friendRequestCounter = 0;
        latestMemoryId = Long.MAX_VALUE;
        initialLoadFriendRequest = MainActivity.getUser().getFriendshipListRequests().size();
        valueEventListenerMemory = getLastMemoryListener();
        DatabaseHandler.getLatestMemory(valueEventListenerMemory);
        childEventListenerFriendRequest = getFriendRequestListener();
        DatabaseHandler.getUserFriendRequest(MainActivity.getUser().getId(), childEventListenerFriendRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceStarted = false;
        DatabaseHandler.removeLatestMemoryListener(valueEventListenerMemory);
        DatabaseHandler.getUserFriendRequest(MainActivity.getUser().getId(), childEventListenerFriendRequest);
    }

    private ChildEventListener getFriendRequestListener() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (friendRequestCounter >= initialLoadFriendRequest) {
                    UserContactInfo friendshipRequest = dataSnapshot.getValue(UserContactInfo.class);
                    String message = friendshipRequest.getDisplayName() + " send you a friend request just now!";
                    NotificationHandler.sendNotification(FirebaseBackgroundService.this, message);
                }
                friendRequestCounter++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return childEventListener;
    }

    private ValueEventListener getLastMemoryListener() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;

                    boolean isNewMemory = memory.getLongId() < latestMemoryId;
                    boolean isFirstLogin = latestMemoryId == Long.MAX_VALUE || memoryCounter == 0;
                    boolean isUsersMemory = memory.getUser().getId().equals(MainActivity.getUser().getId());

                    if (isNewMemory
                            && !isFirstLogin
                            && !isUsersMemory
                            && MainActivity.getUser().getNotificationsOn()) {
                        String message = memory.getUser().getDisplayName() + " uploaded a memory just now!";
                        NotificationHandler.sendNotification(FirebaseBackgroundService.this, message);
                    }
                    latestMemoryId = memory.getLongId();
                    memoryCounter++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return valueEventListener;
    }
}
