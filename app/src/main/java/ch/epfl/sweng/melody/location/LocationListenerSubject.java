package ch.epfl.sweng.melody.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.notification.NotificationHandler;

/**
 * Created by maxwell on 22.11.17.
 */

public class LocationListenerSubject implements LocationListener {

    private ArrayList<LocationObserver> mObservers;
    private static LocationListenerSubject INSTANCE = null;

    private LocationListenerSubject() {
        mObservers = new ArrayList<>();
    }

    public static LocationListenerSubject getLocationListenerInstance(){
        if(INSTANCE == null){
            INSTANCE = new LocationListenerSubject();
        }
        return INSTANCE;
    }

    @Override
    public void onLocationChanged(final Location location) {
        notifyObservers(location);
    }


    @Override
    public void onStatusChanged(String provider, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void registerObserver(LocationObserver locationObserver) {
        mObservers.add(locationObserver);
    }

    public void removeAllObservers() {
        mObservers.clear();
    }

    public void notifyObservers(Location location) {
        for (LocationObserver observer: mObservers) {
            observer.update(location);
        }
    }
}
