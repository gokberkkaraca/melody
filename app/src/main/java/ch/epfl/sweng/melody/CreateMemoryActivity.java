package ch.epfl.sweng.melody;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
    private Spinner dropDown;
    private List<String> tags = new ArrayList<>();
    private List<String> selectedTags = new ArrayList<String>();
    private Bitmap picture;
    private EditText editText;
    private EditText newTag;
    private Uri resourceUri;
    private Memory.MemoryType memoryType;
    private Memory.Privacy memoryPrivacy = Memory.Privacy.PUBLIC;
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
        dropDown = findViewById(R.id.tags_dropdown);
        address = findViewById(R.id.address);
        LocationListenerSubject.getLocationListenerInstance().registerObserver(this);

        fetchTagsFromDatabase();
        selectedTags.add("sweng");

// TODO: Implement UI dropdown that allows you to add new tag as well
//        tagSubmit.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                String addTag = newTag.getText().toString();
//
//                if (addTag.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Cannot add empty tag!", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    for (int i = 0; i < tags.size(); ++i) {
//                        if (tags.get(i).equals(addTag)) {
//                            Toast.makeText(getApplicationContext(), "Tag already exists!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                    addTagToDatabase(addTag);
//                    selectedTags.add(addTag);
//                    fetchTagsFromDatabase();
//                }
//            }
//        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tags);
        dropDown.setAdapter(adapter);
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

    public void makeMemoryPrivate(View view) {
        memoryPrivacy = Memory.Privacy.PRIVATE;
        Toast.makeText(getApplicationContext(), "Memory is private!", Toast.LENGTH_SHORT).show();

    }

    public void makeMemoryPublic(View view) {
        memoryPrivacy = Memory.Privacy.PUBLIC;
        Toast.makeText(getApplicationContext(), "Memory is public!", Toast.LENGTH_SHORT).show();
    }

    public void makeMemoryShared(View view) {
        memoryPrivacy = Memory.Privacy.SHARED;
        Toast.makeText(getApplicationContext(), "Memory is shared!", Toast.LENGTH_SHORT).show();
    }


    public void sendMemory(View view) {
        memoryDescription = editText.getText().toString();
        if (memoryDescription.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Say something!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resourceUri == null) {
            memoryType = Memory.MemoryType.TEXT;
            memory = new Memory.MemoryBuilder(MainActivity.getUser(), memoryDescription, serializableLocation, memoryPrivacy)
                    .tags(selectedTags)
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
                    memory = new Memory.MemoryBuilder(MainActivity.getUser(), memoryDescription, serializableLocation, memoryPrivacy)
                            .photo(url)
                            .tags(selectedTags)
                            .build();
                } else if (memoryType == Memory.MemoryType.VIDEO) {
                    memory = new Memory.MemoryBuilder(MainActivity.getUser(), memoryDescription, serializableLocation, memoryPrivacy)
                            .video(url)
                            .tags(selectedTags)
                            .build();
                    //createAndAddThumbnail(resourceUri);
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

    public static void uploadThumbnail(final String memoryId, Bitmap thumbnail, Context context) {
        Uri thumbnailUri = saveResultToFile("/images", "png", thumbnail, context);
        DatabaseHandler.uploadResource(thumbnailUri, context, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String thumbnailUrl = taskSnapshot.getDownloadUrl().toString();
                DatabaseHandler.setMemoryThumbnail(memoryId, thumbnailUrl);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        }, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {}
        });
    }

    private void createAndAddThumbnail(Uri uri) {
        //Bitmap thumbnail = retrieveVideoFrameFromVideo(uri.getPath());
        //Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(uri.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
        Bitmap thumbnail = retrieveVideoFrameFromVideo(memory.getVideoUrl());
        Uri thumbnailUri = saveResultToFile("/images", "png", thumbnail, this);
        if(thumbnail==null) Toast.makeText(this, "Thumbnail is null", Toast.LENGTH_LONG).show();
        DatabaseHandler.uploadResource(thumbnailUri, this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String thumbnailUrl = taskSnapshot.getDownloadUrl().toString();
                memory.setThumbnailUrl(thumbnailUrl);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        }, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {}
        });
    }

    private Bitmap retrieveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public void fetchTagsFromDatabase() {
        DatabaseHandler.getAllTags(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tagDataSnapshot : dataSnapshot.getChildren()) {
                    String tag = tagDataSnapshot.getValue(String.class);
                    assert tag != null;
                    tags.add(tag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed!");
            }
        });
    }

    public void addTagToDatabase(String newTag) {
        DatabaseHandler.addTag(newTag);
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
        // }
        videoView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(picture);
        if (data != null) resourceUri = data.getData();
        memoryType = Memory.MemoryType.PHOTO;
        }
    }

    private void onPhotoFromCameraResult(Intent data) {
        if (data != null) {
            videoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            assert data.getExtras() != null;
            picture = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(picture);
            memoryType = Memory.MemoryType.PHOTO;
            resourceUri = saveResultToFile("/images", "png", picture, this);
        }
    }

    public static Uri saveResultToFile(String targetFolder, String resourceType, Bitmap pic, Context context) {

        Uri resultUri = null;

        File targetDir = new File(context.getCacheDir().toString() + targetFolder); //File targetDir = new File(this.getCacheDir().toString() + targetFolder);
        targetDir.mkdirs();

        String filename = UUID.randomUUID().toString().substring(0, 8) + "." + resourceType;
        File file = new File(targetDir, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            resultUri = Uri.fromFile(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            Toast.makeText(context, "Error occurred while choosing resource from camera", Toast.LENGTH_LONG).show();
        }

        return resultUri;
    }

    private void onVideoFromGalleryResult(Intent data) {
        if(data != null) {
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            resourceUri = data.getData();
            memoryType = Memory.MemoryType.VIDEO;
            videoView.setVideoURI(data.getData());
            videoView.start();
        }
    }

    private void onVideoFromCameraResult(Intent data) {
        if(data != null) {
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            resourceUri = data.getData();
            memoryType = Memory.MemoryType.VIDEO;
            videoView.setVideoURI(data.getData());
            videoView.start();
        }
    }

    @Override
    public void update(Location location) {
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
}