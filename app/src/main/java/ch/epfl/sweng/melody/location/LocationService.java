package ch.epfl.sweng.melody.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;
import ch.epfl.sweng.melody.util.PermissionUtils;

/**
 * Created by maxwell on 21.11.17.
 */

public class LocationService extends Service {
    public static LocationListenerSubject locationListener;
    private ValueEventListener valueEventListener;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PermissionUtils.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListenerSubject(LocationManager.GPS_PROVIDER);
        try {
            PermissionUtils.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseHandler.removeAllMemoriesListener(valueEventListener);
        locationListener=null;
    }

    public class LocationListenerSubject extends Observable implements LocationListener {

        private Location location;
        private double DISTANCETOUSER = 5000;

        public LocationListenerSubject(String provider) {
            location = new Location(provider);
        }

        public Location getLocation() {
            return location;
        }

        @Override
        public void onLocationChanged(final Location location) {
            this.location = location;
            valueEventListener = new ValueEventListener() {
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
                            String message = "Welcome to revisit " + memory.getSerializableLocation().getLocationName() + " ! See what you did here before!";
                            NotificationHandler.sendNotification(LocationService.this, message);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseHandler.getAllMemories(valueEventListener);
            triggerObservers();
        }


        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        private void triggerObservers() {

            setChanged();
            notifyObservers();
        }
    }

}
