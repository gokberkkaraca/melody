package ch.epfl.sweng.melody.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.Observable;

/**
 * Created by maxwell on 21.11.17.
 */

public class LocationListenerSubject extends Observable implements LocationListener {

    private Location location;

    public LocationListenerSubject(String provider) {
        location = new Location(provider);
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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
