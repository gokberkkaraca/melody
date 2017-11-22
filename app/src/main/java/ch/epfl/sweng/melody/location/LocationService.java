package ch.epfl.sweng.melody.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.RestrictTo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;

/**
 * Created by maxwell on 22.11.17.
 */

public class LocationService extends Service implements LocationObserver {
    @RestrictTo(RestrictTo.Scope.TESTS)
    private static boolean isServiceStarted;
    private double DISTANCETOUSER = 5000;
    private ValueEventListener valueEventListenerLocation;

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
        LocationListenerSubject.getLocationListenerInstance(LocationManager.GPS_PROVIDER).registerObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceStarted = false;
        DatabaseHandler.removeAllMemoriesListener(valueEventListenerLocation);
        LocationListenerSubject.getLocationListenerInstance(LocationManager.GPS_PROVIDER).removeAllObservers();
    }

    @Override
    public void update(final Location location) {
        valueEventListenerLocation = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;
                    double DistanceToUser = memory.getSerializableLocation().
                            distanceTo(new SerializableLocation(location.getLatitude(), location.getLongitude(), ""));
                    boolean isCloseToUser = DistanceToUser < DISTANCETOUSER;
                    boolean isFromSameUser = memory.getUser().equals(MainActivity.getUser());
                    if (isCloseToUser && isFromSameUser) {
                        String message = "Welcome back to " + memory.getSerializableLocation().getLocationName() + " !";
                        NotificationHandler.sendNotification(LocationService.this, message);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseHandler.getAllMemories(valueEventListenerLocation);
    }
}
