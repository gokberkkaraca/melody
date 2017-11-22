package ch.epfl.sweng.melody.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.PublicMemoryActivity;

import static android.content.Context.LOCATION_SERVICE;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_GPS;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_GALLERY;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_VIDEO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_VIDEO_GALLERY;

/**
 * Created by maxwell on 16.11.17.
 */

public class DialogUtils {

    public static void pickVideoDialog(final AppCompatActivity activity) {
        final CharSequence[] options = {"Camera", "Choose from Album", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add videos");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Camera")) {
                    PermissionUtils.accessResourceWithPermission(activity, REQUEST_VIDEO_CAMERA);
                } else if (options[option].equals("Choose from Album")) {
                    PermissionUtils.accessResourceWithPermission(activity, REQUEST_VIDEO_GALLERY);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void pickPhotoDialog(final AppCompatActivity activity) {
        final CharSequence[] options = {"Camera", "Choose from Album", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add photos");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Camera")) {
                    PermissionUtils.accessResourceWithPermission(activity, REQUEST_PHOTO_CAMERA);
                } else if (options[option].equals("Choose from Album")) {
                    PermissionUtils.accessResourceWithPermission(activity, REQUEST_PHOTO_GALLERY);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void showGPSDisabledDialog(final Activity activity) {
        LocationManager service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        assert service != null;
        boolean isLocationEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isLocationEnabled) {
            new AlertDialog.Builder(activity)
                    .setTitle("Open GPS")
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

    public static void showLocationPermissionRationale(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Required")
                .setMessage("Melody can't continue without location permission. If you want to create memories, please activate permissions")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(activity, PublicMemoryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(MainActivity.USER_INFO, MainActivity.getUser());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                })
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PermissionUtils.requestLocationPermission(activity);
                    }
                })
                .setCancelable(false)
                .show();
    }
}
