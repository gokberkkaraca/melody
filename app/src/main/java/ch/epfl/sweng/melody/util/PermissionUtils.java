package ch.epfl.sweng.melody.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by maxwell on 16.11.17.
 */

public class PermissionUtils {
    public static final int REQUEST_PHOTO_GALLERY = 1;
    public static final int REQUEST_PHOTO_CAMERA = 2;
    public static final int REQUEST_VIDEO_GALLERY = 3;
    public static final int REQUEST_VIDEO_CAMERA = 4;
    public static final int REQUEST_AUDIOFILE = 5;
    public static final int REQUEST_GPS = 6;
    public static final int REQUEST_LOCATION = 7;


    public static void accessResourceWithPermission(AppCompatActivity activity, int requestCode) {
        switch (requestCode) {
            case REQUEST_PHOTO_CAMERA: {
                if (permissionNotGranted(activity, Manifest.permission.CAMERA))
                    requestPermission(activity, Manifest.permission.CAMERA, REQUEST_PHOTO_CAMERA);
                else
                    photoFromCamera(activity);
                break;
            }
            case REQUEST_VIDEO_CAMERA: {
                if (permissionNotGranted(activity, Manifest.permission.CAMERA))
                    requestPermission(activity, Manifest.permission.CAMERA, REQUEST_VIDEO_CAMERA);
                else
                    videoFromCamera(activity);
                break;
            }
            case REQUEST_PHOTO_GALLERY: {
                if (permissionNotGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
                    requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PHOTO_GALLERY);
                else
                    photoFromGallery(activity);
                break;
            }
            case REQUEST_VIDEO_GALLERY: {
                if (permissionNotGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
                    requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_VIDEO_GALLERY);
                else
                    videoFromGallery(activity);
                break;
            }
            case REQUEST_AUDIOFILE: {
                if (permissionNotGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
                    requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_AUDIOFILE);
                else
                    accessAudioFiles(activity);
                break;
            }
        }

    }

    public static void requestLocationPermission(AppCompatActivity activity){
        if(permissionNotGranted(activity,Manifest.permission.ACCESS_FINE_LOCATION))
            requestPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_LOCATION);
    }

    public static void photoFromCamera(AppCompatActivity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_PHOTO_CAMERA);
    }

    public static void photoFromGallery(AppCompatActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_PHOTO_GALLERY);
    }

    public static void videoFromCamera(AppCompatActivity activity) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_VIDEO_CAMERA);
    }

    public static void videoFromGallery(AppCompatActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_VIDEO_GALLERY);
    }

    public static void accessAudioFiles(AppCompatActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_AUDIOFILE);
    }

    private static boolean permissionNotGranted(AppCompatActivity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermission(AppCompatActivity activity, String permission, int resquestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, resquestCode);
    }
}
