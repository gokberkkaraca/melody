package ch.epfl.sweng.melody;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.User;

public class CreateMemoryActivity extends AppCompatActivity{
    private static final int REQUEST_PHOTO_GALLERY = 1;
    private static final int REQUEST_PHOTO_CAMERA = 2;
    private static final int REQUEST_VIDEO_GALLERY = 3;
    private static final int REQUEST_VIDEO_CAMERA = 4;
    private static final int REQUEST_AUDIOFILE = 5;
    private static final String FAKE_ADDRESS = "Lausanne,Switzerland";
    User user;
    private ImageView imageView;
    private VideoView videoView;
    private Bitmap picture;
    private EditText editText;
    private Uri resourceUri;
    private Memory.MemoryType memoryType;
    private String memoryDescription;
    private Memory memory;
    private String audioPath;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_INFO);

        imageView = (ImageView) findViewById(R.id.display_chosen_photo);
        videoView = (VideoView) findViewById(R.id.display_chosen_video);
        editText = (EditText) findViewById(R.id.memory_description);
        address = (TextView) findViewById(R.id.address);
        address.setText(FAKE_ADDRESS);

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

    public void sendMemory(View view) {
        memoryDescription = editText.getText().toString();
        if (memoryDescription.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Say something!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resourceUri == null) {
            memoryType = Memory.MemoryType.TEXT;
            memory = new Memory.MemoryBuilder(user.getId(), memoryDescription, FAKE_ADDRESS)
                    .build();
            DatabaseHandler.uploadMemory(memory);
            Toast.makeText(getApplicationContext(), "Memory uploaded!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateMemoryActivity.this, PublicMemoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MainActivity.USER_INFO, user);
            intent.putExtras(bundle);
            startActivity(intent);
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
                    memory = new Memory.MemoryBuilder(user.getId(), memoryDescription, FAKE_ADDRESS)
                            .photo(url)
                            .build();
                } else if (memoryType == Memory.MemoryType.VIDEO) {
                    memory = new Memory.MemoryBuilder(user.getId(), memoryDescription, FAKE_ADDRESS)
                            .build();
                }
                DatabaseHandler.uploadMemory(memory);
                Intent intent = new Intent(CreateMemoryActivity.this, PublicMemoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MainActivity.USER_INFO, user);
                intent.putExtras(bundle);
                startActivity(intent);
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
            case REQUEST_AUDIOFILE: {
                //   onAudioFileResult(data);
                break;
            }
        }
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
                case REQUEST_AUDIOFILE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        accessAudioFiles();
                        break;
                    }
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
            case REQUEST_AUDIOFILE: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_AUDIOFILE);
                } else {
                    accessAudioFiles();
                }
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
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
        resourceUri = data.getData();
        memoryType = Memory.MemoryType.PHOTO;
    }

    private void accessAudioFiles() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_AUDIOFILE);
    }

    private void onPhotoFromCameraResult(Intent data) {
        picture = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(picture);
    }

  /*  private void onAudioFileResult(Intent data) {
        if (data != null) {
            try {
                File audio = MediaStore.Audio.Media.getContentUriForPath()
                // what to  get ??
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } */

    public void pickAudioDialog(View view) {
        final CharSequence[] options = {"Record", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add audio");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Record")) {
                    Intent intent = new Intent(CreateMemoryActivity.this, AudioRecordingActivity.class);
                    CreateMemoryActivity.this.startActivity(intent);
                } else if (options[option].equals("Choose from Library")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_AUDIOFILE);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onVideoFromGalleryResult(Intent data) {
        resourceUri = data.getData();
        memoryType = Memory.MemoryType.VIDEO;
        videoView.setVideoURI(data.getData());
        videoView.start();
    }

    private void onVideoFromCameraResult(Intent data) {
        videoView.setVideoURI(data.getData());
        videoView.start();
    }
}