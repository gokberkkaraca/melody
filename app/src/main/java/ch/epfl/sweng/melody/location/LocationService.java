package ch.epfl.sweng.melody.location;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import ch.epfl.sweng.melody.util.PermissionUtils;

/**
 * Created by maxwell on 21.11.17.
 */

public class LocationService extends Service {
    public static LocationListenerSubject locationListener;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PermissionUtils.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListenerSubject(LocationManager.GPS_PROVIDER, LocationService.this);
        try {
            PermissionUtils.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
