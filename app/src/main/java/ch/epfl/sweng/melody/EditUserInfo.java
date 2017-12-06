package ch.epfl.sweng.melody;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.MenuButtons;

import static ch.epfl.sweng.melody.database.DatabaseHandler.uploadUser;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_GALLERY;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_VIDEO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_VIDEO_GALLERY;
import static ch.epfl.sweng.melody.util.PermissionUtils.photoFromCamera;
import static ch.epfl.sweng.melody.util.PermissionUtils.photoFromGallery;
import static ch.epfl.sweng.melody.util.PermissionUtils.videoFromCamera;
import static ch.epfl.sweng.melody.util.PermissionUtils.videoFromGallery;

public class EditUserInfo extends AppCompatActivity {
    private User user;
    private EditText displayName;
    private EditText userBio;
    private Uri profileUri;
    private Uri backgroundUri;
    private ImageView profileImage;
    private ImageView backgroundImage;
    private Bitmap picture;
    private Uri resourceUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        setTitle("");
        Toolbar editToorbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(editToorbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = MainActivity.getUser();
        displayName = findViewById(R.id.change_display_name);
        userBio = findViewById(R.id.change_user_bio);
        profileImage = findViewById(R.id.profile_image);
        backgroundImage = findViewById(R.id.background_image);

        displayName.setText(user.getDisplayName());
        displayName.setTextColor(Color.GRAY);
        userBio.setText(user.getBiography());

        Picasso.with(getApplicationContext()).load(user.getProfilePhotoUrl()).into(profileImage);
        Picasso.with(getApplicationContext()).load(user.getBackgroundPhotoUrl()).into(backgroundImage);
    }


    public void changeBackgroundImage (View view) {
        picture=null;
        DialogUtils.pickPhotoDialog(this);
        backgroundImage.setImageBitmap(picture);
    }

    public void changeProfileImage (View view) {
        picture=null;
        DialogUtils.pickPhotoDialog(this);
        profileImage.setImageBitmap(picture);
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
        resourceUri = data.getData();
//        return picture;
    }

    private void onPhotoFromCameraResult(Intent data) {
        assert data.getExtras() != null;
        picture = (Bitmap) data.getExtras().get("data");
        resourceUri = saveResultToFile("/images", "png");
    }

    private Uri saveResultToFile(String targetFolder, String resourceType) {

        Uri resultUri = null;

        File targetDir = new File(this.getCacheDir().toString() + targetFolder);
        targetDir.mkdirs();

        String filename = UUID.randomUUID().toString().substring(0, 8) + "." + resourceType;
        File file = new File(targetDir, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            picture.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            resultUri = Uri.fromFile(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            Toast.makeText(this, "Error occurred while choosing resource from camera", Toast.LENGTH_LONG).show();
        }

        return resultUri;
    }

    public void changeUserProfile (View view) {
        String name = displayName.getText().toString();
        String bio = userBio.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            user.setDisplayName(name);
        }
        if (!TextUtils.isEmpty(bio)) {
            if(bio.length() > 500){
                Toast.makeText(getApplicationContext(), R.string.bio_too_long, Toast.LENGTH_SHORT).show();
                return;
            }else {
                user.setBiograhy(bio);
            }
        }

        if (profileUri != null) {
            DatabaseHandler.uploadResource(profileUri, this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "User profile saved!", Toast.LENGTH_SHORT).show();
                    String url = taskSnapshot.getDownloadUrl().toString();
                    user.setProfilePhotoUrl(url);
                    DatabaseHandler.uploadUser(user);
                    MenuButtons.goToUserProfileActivity(EditUserInfo.this);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }

        uploadUser(user);
        FirebaseUser firebaseUser = MainActivity.initializeFirebaseAuth().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).setPhotoUri(profileUri)
                .build();
        firebaseUser.updateProfile(profileChangeRequest);
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToUserProfileActivity(this);
    }

}
