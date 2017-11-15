package ch.epfl.sweng.melody.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.RestrictTo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;
import ch.epfl.sweng.melody.user.User;

/**
 * Created by maxwell on 02.11.17.
 */

public class FirebaseBackgroundService extends Service {
    @RestrictTo(RestrictTo.Scope.TESTS)
    private static boolean isServiceStarted;

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
        DatabaseHandler.getLatestMemory(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;
                    String message = User.docodeIdtoEmail(memory.getAuthorId()) + " upload a memory just now!";
                    NotificationHandler
                            .sendNotification(FirebaseBackgroundService.this, message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
