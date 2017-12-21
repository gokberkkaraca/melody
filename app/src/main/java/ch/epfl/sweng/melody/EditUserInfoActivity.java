package ch.epfl.sweng.melody;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.NavigationHandler;

import static ch.epfl.sweng.melody.database.DatabaseHandler.uploadUser;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_CAMERA;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_PHOTO_GALLERY;
import static ch.epfl.sweng.melody.util.PermissionUtils.photoFromCamera;
import static ch.epfl.sweng.melody.util.PermissionUtils.photoFromGallery;

public class EditUserInfoActivity extends AppCompatActivity {
    private EditText displayName;
    private EditText userBio;
    private Uri profileUri;
    private ImageView profileImage;
    private Bitmap picture;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        user = MainActivity.getUser();
        setTitle("");
        Toolbar editToorbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(editToorbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        displayName = findViewById(R.id.change_display_name);
        userBio = findViewById(R.id.change_user_bio);
        profileImage = findViewById(R.id.profile_image);

        displayName.setHint(user.getDisplayName());
        displayName.setHintTextColor(Color.GRAY);
        String bio = user.getBiography();
        if (bio.isEmpty()) {
            userBio.setHint(R.string.describe_yourself);
            userBio.setHintTextColor(Color.GRAY);
        } else {
            userBio.setText(bio);
        }

        Picasso.with(getApplicationContext()).load(user.getProfilePhotoUrl()).into(profileImage);
    }

    public void changeProfileImage(View view) {
        picture = null;
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
        assert data != null;
        profileUri = data.getData();
        profileImage.setImageBitmap(picture);
    }

    private void onPhotoFromCameraResult(Intent data) {
        assert data.getExtras() != null;
        picture = (Bitmap) data.getExtras().get("data");
        profileUri = CreateMemoryActivity.saveResultToFile("/images", "png",picture,this);
        profileImage.setImageBitmap(picture);
    }

    public void changeUserProfile(View view) {
        String name = displayName.getText().toString();
        String bio = userBio.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            MainActivity.getUser().setDisplayName(name);
        }
        if (!TextUtils.isEmpty(bio)) {
            MainActivity.getUser().setBiograhy(bio);
        }

        if (profileUri != null) {
            DatabaseHandler.uploadResource(profileUri, this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "User profile saved!", Toast.LENGTH_SHORT).show();
                    Uri url = taskSnapshot.getDownloadUrl();
                    assert url != null;
                    MainActivity.getUser().setProfilePhotoUrl(url.toString());
                    DatabaseHandler.uploadUser(MainActivity.getUser());
                    NavigationHandler.goToUserProfileActivity(EditUserInfoActivity.this);
                    FirebaseUser firebaseUser = MainActivity.getFirebaseAuthInstance().getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(url)
                            .build();
                    assert firebaseUser != null;
                    firebaseUser.updateProfile(profileChangeRequest);
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

        uploadUser(MainActivity.getUser());
        FirebaseUser firebaseUser = MainActivity.getFirebaseAuthInstance().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        assert firebaseUser != null;
        firebaseUser.updateProfile(profileChangeRequest);

        NavigationHandler.goToUserProfileActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationHandler.goToUserProfileActivity(this);
    }

}
