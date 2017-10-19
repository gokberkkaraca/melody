package ch.epfl.sweng.melody;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateMemoryActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_PHOTO_GALLERY = 1;
    private static final int REQUEST_PHOTO_CAMERA = 2;
    private static final int REQUEST_VIDEO_GALLERY = 3;
    private static final int REQUEST_VIDEO_CAMERA = 4;
    private ImageView imageView;
    private VideoView videoView;
    private Bitmap picture;

//    private TextView latitudeField;
//    private TextView longitudeField;
    private TextView addressField; //Add a new TextView to your activity_main to display the address
    private LocationManager locationManager;
    private String provider;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        imageView = (ImageView) findViewById(R.id.display_chosen_photo);
        videoView = (VideoView) findViewById(R.id.display_chosen_video);

//        latitudeField = (TextView) findViewById(R.id.latitude);
//        longitudeField = (TextView) findViewById(R.id.longitude);
        addressField = (TextView) findViewById(R.id.address);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        addressField.setText(provider);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
//            latitudeField.setText("Location not available");
//            longitudeField.setText("Location not available");
        }
    }

    public void pickPhotoDialog(View view) {
        final CharSequence[] options = {"Camera", "Choose from Album", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add photos");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Camera")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_PHOTO_CAMERA);
                } else if (options[option].equals("Choose from Album")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_PHOTO_GALLERY);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void pickVideoDialog(View view) {
        final CharSequence[] options = {"Camera", "Choose from Album", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add videos");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Camera")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_VIDEO_CAMERA);
                } else if (options[option].equals("Choose from Album")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_VIDEO_GALLERY);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PHOTO_CAMERA: {
                onPhotoFromCameraResult(data);
                break;
            }
            case REQUEST_PHOTO_GALLERY: {
                onPhotoFromGalleryResult(data);
                break;
            }
            case REQUEST_VIDEO_CAMERA: {
                onVideoFromCameraResult(data);
                break;
            }
            case REQUEST_VIDEO_GALLERY: {
                onVideoFromGalleryResult(data);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
            String finalAddress = addresses.get(0).getCountryName() + ", "+ addresses.get(0).getLocality();
//            latitudeField.setText(String.valueOf(lat));
//            longitudeField.setText(String.valueOf(lng));
            addressField.setText(finalAddress);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_PHOTO_CAMERA: {
                    photoFromCamera();
                    break;
                }
                case REQUEST_PHOTO_GALLERY: {
                    photoFromGallery();
                    break;
                }
                case REQUEST_VIDEO_CAMERA: {
                    videoFromCamera();
                    break;
                }
                case REQUEST_VIDEO_GALLERY: {
                    videoFromGallery();
                    break;
                }
            }
        }
    }

    private void accessWithPermission(Context context, int resquestCode) {
        switch (resquestCode) {
            case REQUEST_PHOTO_CAMERA: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_PHOTO_CAMERA);
                } else {
                    photoFromCamera();
                }
                break;
            }
            case REQUEST_VIDEO_CAMERA: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_VIDEO_CAMERA);
                } else {
                    videoFromCamera();
                }
                break;
            }
            case REQUEST_PHOTO_GALLERY: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PHOTO_GALLERY);
                } else {
                    photoFromGallery();
                }
                break;
            }
            case REQUEST_VIDEO_GALLERY: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_VIDEO_GALLERY);
                } else {
                    videoFromGallery();
                }
                break;
            }
        }
    }

    private void photoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PHOTO_CAMERA);
    }

    private void photoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PHOTO_GALLERY);
    }

    private void videoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_VIDEO_CAMERA);
    }

    private void videoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_VIDEO_GALLERY);
    }

    private void onPhotoFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();// this one is not good and need to be discussed
            }
        }
        imageView.setImageBitmap(picture);
    }

    private void onPhotoFromCameraResult(Intent data) {
        picture = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(picture);
    }

    private void onVideoFromGalleryResult(Intent data) {
        videoView.setVideoURI(data.getData());
        videoView.start();
    }

    private void onVideoFromCameraResult(Intent data) {
        videoView.setVideoURI(data.getData());
        videoView.start();
    }


}