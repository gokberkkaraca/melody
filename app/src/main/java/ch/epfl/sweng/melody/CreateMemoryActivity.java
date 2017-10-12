package ch.epfl.sweng.melody;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class CreateMemoryActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap picture;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    public static final String SEND_TEXT_MESSAGE = "ch.epfl.sweng.melody.TEXT";
    public static final String SEND_PHOTO_MESSAGE = "ch.epfl.sweng.melody.PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        imageView = (ImageView) findViewById(R.id.display_chosen_photo);
    }

    public void pickPhotoDialog(View view) {
        final CharSequence[] options = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Take Photo")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_CAMERA);
                } else if (options[option].equals("Choose from Library")) {
                    accessWithPermission(CreateMemoryActivity.this, REQUEST_GALLERY);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void sendMemory(View view) {
        Intent intent = new Intent(this, PublicMemoryActivity.class);
        EditText editText = (EditText) findViewById(R.id.memory_description);
        String message = editText.getText().toString();
        intent.putExtra(SEND_TEXT_MESSAGE, message);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
        intent.putExtra(SEND_PHOTO_MESSAGE, picture);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY)
                onGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCameraResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessCamera();
                }
                break;
            }
            case REQUEST_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessGallery();
                }
            }
        }
    }

    private void accessWithPermission(Context context, int resquestCode) {
        switch (resquestCode) {
            case REQUEST_CAMERA: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA);
                } else {
                    accessCamera();
                }
                break;
            }
            case REQUEST_GALLERY: {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_GALLERY);
                } else {
                    accessGallery();
                }
            }

        }
    }

    private void accessCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void accessGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void onGalleryResult(Intent data) {
        if (data != null) {
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();// this one is not good and need to be discussed
            }
        }
        imageView.setImageBitmap(picture);
    }

    private void onCameraResult(Intent data) {
        picture = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(picture);
    }

}