package ch.epfl.sweng.melody.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by maxwell on 21.11.17.
 */

public class LocationListenerSubject implements LocationListener {
    private List<LocationObserver> locationObservers;

    public LocationListenerSubject(String provider) {
        Log.e(TAG, "LocationListener " + provider);
        locationObservers = new ArrayList<>();
    }

    public void attachLocationObserver(LocationObserver locationObserver){
        locationObservers.add(locationObserver);
    }

    public void detachLocationObserver(LocationObserver locationObserver){
        locationObservers.remove(locationObserver);
    }

    private void notifyLocationChanges(Location location){
        for(LocationObserver locationObserver: locationObservers){
            locationObserver.update(location);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);
        notifyLocationChanges(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }
}
