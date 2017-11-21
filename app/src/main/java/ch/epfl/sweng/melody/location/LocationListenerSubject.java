package ch.epfl.sweng.melody.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;

/**
 * Created by maxwell on 21.11.17.
 */

public class LocationListenerSubject extends Observable implements LocationListener {

    private Location location;
    private Context context;
    private double DISTANCETOUSER = 5000;

    public LocationListenerSubject(String provider, Context context) {
        location = new Location(provider);
        this.context = context;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(final Location location) {
        this.location = location;
        ValueEventListener valueEventListener = new ValueEventListener() {
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
                        NotificationHandler.sendNotification(context, message);
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
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void triggerObservers() {

        setChanged();
        notifyObservers();
    }
}
