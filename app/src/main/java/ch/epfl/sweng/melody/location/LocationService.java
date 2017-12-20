package ch.epfl.sweng.melody.location;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.RestrictTo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;


public class LocationService extends Service implements LocationObserver {
    @RestrictTo(RestrictTo.Scope.TESTS)
    private static boolean isServiceStarted;
    Date lastTimestamp = new Date();
    private double DISTANCE_TO_USER = 5000;
    private double MIN_DISTANCE = 500;
    private long TIME_PERIOD = MILLISECONDS.convert(0, MINUTES);
    private SerializableLocation lastLocation = new SerializableLocation(0, 0, "FAKE");
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
        LocationListenerSubject.getLocationListenerInstance().registerObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceStarted = false;
        DatabaseHandler.removeAllMemoriesListener(valueEventListenerLocation);
        LocationListenerSubject.getLocationListenerInstance().removeAllObservers();
    }

    @Override
    public void update(final Location location) {
        SerializableLocation newLocation = new SerializableLocation(location.getLatitude(), location.getLongitude(), "NOW");
        Date currentTimeStamp = new Date();

        long timeDiff = currentTimeStamp.getTime() - lastTimestamp.getTime();

        if (timeDiff >= TIME_PERIOD) {
            if (newLocation.distanceTo(lastLocation) > MIN_DISTANCE && MainActivity.getUser().getNotificationsOn()) {
                valueEventListenerLocation = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                            Memory memory = memDataSnapshot.getValue(Memory.class);
                            assert memory != null;
                            double DistanceToUser = memory.getSerializableLocation().
                                    distanceTo(new SerializableLocation(location.getLatitude(), location.getLongitude(), ""));
                            boolean isCloseToUser = DistanceToUser < DISTANCE_TO_USER;
                            boolean isFromSameUser = memory.getUser().equals(MainActivity.getUser());
                            if (isCloseToUser && isFromSameUser) {
                                String message = "Welcome back to " + memory.getSerializableLocation().getLocationName() + " !";
                                NotificationHandler.sendNotification(LocationService.this, message);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                DatabaseHandler.getAllMemoriesWithSingleListener(valueEventListenerLocation);
            }
        }
        lastLocation = newLocation;
        lastTimestamp = currentTimeStamp;
    }


}
