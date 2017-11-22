package ch.epfl.sweng.melody;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.location.LocationListenerSubject;
import ch.epfl.sweng.melody.location.LocationObserver;
import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.MenuButtons;

import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_GALLERY;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_VIDEO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_VIDEO_GALLERY;
import static ch.epfl.sweng.melody.util.PermissionUtils.photoFromCamera;
import static ch.epfl.sweng.melody.util.PermissionUtils.photoFromGallery;
import static ch.epfl.sweng.melody.util.PermissionUtils.videoFromCamera;
import static ch.epfl.sweng.melody.util.PermissionUtils.videoFromGallery;

public class CreateMemoryActivity extends AppCompatActivity implements LocationObserver {

    TextView address;
    private ImageView imageView;
    private VideoView videoView;
    private Bitmap picture;
    private EditText editText;
    private Uri resourceUri;
    private Memory.MemoryType memoryType;
    private String memoryDescription;
    private Memory memory;
    private SerializableLocation serializableLocation = new SerializableLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        imageView = findViewById(R.id.display_chosen_photo);
        videoView = findViewById(R.id.display_chosen_video);
        editText = findViewById(R.id.memory_description);
        address = findViewById(R.id.address);
        LocationListenerSubject.getLocationListenerInstance().registerObserver(this);
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToPublicMemoryActivity(this);
    }


    public void pickVideoDialog(View view) {
        DialogUtils.pickVideoDialog(this);
    }

    public void pickPhotoDialog(View view) {
        DialogUtils.pickPhotoDialog(this);
    }

    public void sendMemory(View view) {
        memoryDescription = editText.getText().toString();
        if (memoryDescription.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Say something!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resourceUri == null) {
            memoryType = Memory.MemoryType.TEXT;
            memory = new Memory.MemoryBuilder(MainActivity.getUser(), memoryDescription, serializableLocation)
                    .build();
            DatabaseHandler.uploadMemory(memory);
            Toast.makeText(getApplicationContext(), "Memory uploaded!", Toast.LENGTH_SHORT).show();
            MenuButtons.goToPublicMemoryActivity(CreateMemoryActivity.this);
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Memory...");
        progressDialog.show();
        DatabaseHandler.uploadResource(resourceUri, this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Memory uploaded!", Toast.LENGTH_SHORT).show();
                String url = taskSnapshot.getDownloadUrl().toString();
                if (memoryType == Memory.MemoryType.PHOTO) {
                    memory = new Memory.MemoryBuilder(MainActivity.getUser(), memoryDescription, serializableLocation)
                            .photo(url)
                            .build();
                } else if (memoryType == Memory.MemoryType.VIDEO) {
                    memory = new Memory.MemoryBuilder(MainActivity.getUser(), memoryDescription, serializableLocation)
                            .video(url)
                            .build();
                }
                DatabaseHandler.uploadMemory(memory);
                MenuButtons.goToPublicMemoryActivity(CreateMemoryActivity.this);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_PHOTO_CAMERA: {
                    photoFromCamera(this);
                    break;
                }
                case REQUEST_PHOTO_GALLERY: {
                    photoFromGallery(this);
                    break;
                }
                case REQUEST_VIDEO_CAMERA: {
                    videoFromCamera(this);
                    break;
                }
                case REQUEST_VIDEO_GALLERY: {
                    videoFromGallery(this);
                    break;
                }
            }
        }
    }

    private void onPhotoFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();// this one is not good and need to be discussed
            }
        }
        videoView.setVisibility(View.GONE);
        imageView.setImageBitmap(picture);
        resourceUri = data.getData();
        memoryType = Memory.MemoryType.PHOTO;
    }

    private void onPhotoFromCameraResult(Intent data) {
        picture = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(picture);
    }

    private void onVideoFromGalleryResult(Intent data) {
        resourceUri = data.getData();
        memoryType = Memory.MemoryType.VIDEO;
        videoView.setVideoURI(data.getData());
        imageView.setVisibility(View.GONE);
        videoView.start();
    }

    private void onVideoFromCameraResult(Intent data) {
        videoView.setVideoURI(data.getData());
        videoView.start();
    }

    @Override
    public void update(Location location) {
//        if (observable instanceof LocationListenerSubject) {
//            LocationListenerSubject locationSubject = (LocationListenerSubject) observable;
//            Location location = locationSubject.getLocation();
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String addressText = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryCode();
                address.setText(addressText);
                serializableLocation.setLatitude(location.getLatitude());
                serializableLocation.setLongitude(location.getLongitude());
                serializableLocation.setLocationName(addressText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//    }
}