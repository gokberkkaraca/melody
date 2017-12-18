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
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;


public class FirebaseBackgroundService extends Service {
    @RestrictTo(RestrictTo.Scope.TESTS)
    private static boolean isServiceStarted;
    private static ValueEventListener valueEventListenerMemory;
    private static ChildEventListener childEventListenerFriendRequest;
    private static ChildEventListener childEventListenerFriendList;
    private long memoryCounter;
    private long latestMemoryId;
    private long initialLoadFriendRequestNum;
    private long friendRequestCounter;
    private long initialLoadFriendListNum;
    private long friendCounter;

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
        friendCounter = 0;
        latestMemoryId = Long.MAX_VALUE;
        initialLoadFriendRequestNum = MainActivity.getUser().getFriendshipListRequests().size();
        initialLoadFriendListNum = MainActivity.getUser().getFriends().size();
        valueEventListenerMemory = getLastMemoryListener();
        DatabaseHandler.getLatestMemory(valueEventListenerMemory);
        childEventListenerFriendRequest = getFriendRequestListener();
        DatabaseHandler.getUserFriendRequest(MainActivity.getUser().getId(), childEventListenerFriendRequest);
        childEventListenerFriendList = getFriendsListListener();
        DatabaseHandler.getUserFriendList(MainActivity.getUser().getId(), childEventListenerFriendList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceStarted = false;
        DatabaseHandler.removeLatestMemoryListener(valueEventListenerMemory);
        DatabaseHandler.removeUserFriendRequestListener(MainActivity.getUser().getId(), childEventListenerFriendRequest);
        DatabaseHandler.removeUserFriendListListener(MainActivity.getUser().getId(), childEventListenerFriendRequest);
    }

    private ChildEventListener getFriendRequestListener() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (friendRequestCounter < initialLoadFriendRequestNum) {
                    friendRequestCounter++;
                } else {
                    UserContactInfo friendshipRequest = dataSnapshot.getValue(UserContactInfo.class);
                    String message = friendshipRequest.getDisplayName() + " send you a friend request just now!";
                    NotificationHandler.sendNotification(FirebaseBackgroundService.this, message);
                }
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

    private ChildEventListener getFriendsListListener() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (friendCounter < initialLoadFriendListNum) {
                    friendCounter++;
                } else {
                    UserContactInfo friendshipRequest = dataSnapshot.getValue(UserContactInfo.class);
                    String message = friendshipRequest.getDisplayName() + " is your friend now!";
                    NotificationHandler.sendNotification(FirebaseBackgroundService.this, message);
                }
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
                    final Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;

                    final boolean isNewMemory = memory.getLongId() < latestMemoryId;
                    final boolean isFirstLogin = latestMemoryId == Long.MAX_VALUE || memoryCounter == 0;
                    final boolean isUsersMemory = memory.getUser().getId().equals(MainActivity.getUser().getId());
                    final boolean isNotPrivate = memory.getPrivacy() != Memory.Privacy.PRIVATE;
                    DatabaseHandler.getUser(memory.getUser().getId(), new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            boolean isFromFriend = user.isFriendWith(MainActivity.getUser());
                            if (isNewMemory
                                    && !isFirstLogin
                                    && !isUsersMemory
                                    && MainActivity.getUser().getNotificationsOn()
                                    && isFromFriend
                                    && isNotPrivate) {
                                String message = memory.getUser().getDisplayName() + " uploaded a memory just now!";
                                NotificationHandler.sendNotification(FirebaseBackgroundService.this, message);
                            }
                            latestMemoryId = memory.getLongId();
                            memoryCounter++;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return valueEventListener;
    }
}
