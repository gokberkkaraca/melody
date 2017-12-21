package ch.epfl.sweng.melody.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.ArrayList;


public class LocationListenerSubject implements LocationListener {

    private static LocationListenerSubject INSTANCE = null;
    private ArrayList<LocationObserver> mObservers;

    private LocationListenerSubject() {
        mObservers = new ArrayList<>();
    }

    public static LocationListenerSubject getLocationListenerInstance() {
        if (INSTANCE == null) {
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

    void removeAllObservers() {
        mObservers.clear();
    }

    void notifyObservers(Location location) {
        for (LocationObserver observer : mObservers) {
            observer.update(location);
        }
    }
}
