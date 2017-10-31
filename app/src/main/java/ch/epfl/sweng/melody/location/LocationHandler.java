package ch.epfl.sweng.melody.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import static android.content.Context.LOCATION_SERVICE;
import static ch.epfl.sweng.melody.util.RequestCodes.*;

public class LocationHandler implements LocationListener {

    private final Activity activity;

    public LocationHandler(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        if (provider.equals(LocationManager.GPS_PROVIDER))
        {
            showGPSDisabledDialog();
        }
    }

    public void showGPSDisabledDialog() {
        LocationManager service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        assert service != null;
        boolean isLocationEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isLocationEnabled) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity);
            builder.setTitle("Open GPS")
                    .setMessage("Creating a new memory requires Melody to access your location. Do you want to activate Location Services?")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finishAffinity();
                        }
                    })
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivityForResult(intent, REQUEST_GPS);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }
}
