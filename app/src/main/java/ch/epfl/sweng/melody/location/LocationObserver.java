package ch.epfl.sweng.melody.location;

import android.location.Location;


public interface LocationObserver {
    void update(Location location);
}
