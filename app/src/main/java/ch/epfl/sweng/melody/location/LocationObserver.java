package ch.epfl.sweng.melody.location;

import android.location.Location;

/**
 * Created by maxwell on 21.11.17.
 */

public interface LocationObserver {
    void update(Location location);
}
