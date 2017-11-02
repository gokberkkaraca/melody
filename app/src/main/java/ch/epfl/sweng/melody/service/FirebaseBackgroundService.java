package ch.epfl.sweng.melody.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.notification.NotificationHandler;

/**
 * Created by maxwell on 02.11.17.
 */

public class FirebaseBackgroundService extends Service {
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NotificationHandler.sendNotification(FirebaseBackgroundService.this,"There is a new memory!");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
