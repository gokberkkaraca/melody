package ch.epfl.sweng.melody;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.melody.memory.GoogleProfilePictureAsync;

public class PublicMemoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_public_memory);

        Intent memoryIntent = getIntent();
        String text = memoryIntent.getStringExtra(CreateMemoryActivity.SEND_TEXT_MESSAGE);
//        byte[] picture_byte = intent.getByteArrayExtra(CreateMemoryActivity.SEND_PHOTO_MESSAGE);
//        Bitmap picture = BitmapFactory.decodeByteArray(picture_byte,0,picture_byte.length);
        Bitmap picture = memoryIntent.getParcelableExtra(CreateMemoryActivity.SEND_PHOTO_MESSAGE);

        //setSupportActionBar(toolbar);


        ImageView photo = new ImageView(this);
        photo.setImageBitmap(picture);
        addPhotoMemory(photo);
        addTextMemory("my memory 1");
        addTextMemory("my memory 2");
        addTextMemory("my memory 3");
        addTextMemory("my memory 4");
        addTextMemory("my memory 5");

    }

    public void addPhotoMemory(ImageView photo) {
        LinearLayout memoriesParent = (LinearLayout) findViewById(R.id.publicMemoryActivity_LinearLayout_Memories);
        LinearLayout memPhoto = new LinearLayout(this);
        memPhoto.addView(photo);
        addMemoryContainer(memoriesParent, memPhoto);
    }

    public void addTextMemory(String txt) {
        LinearLayout memoriesParent = (LinearLayout) findViewById(R.id.publicMemoryActivity_LinearLayout_Memories);
        LinearLayout mem = new LinearLayout(this);
        TextView txtMem = new TextView(this);
        txtMem.setText(txt);
        mem.addView(txtMem);
        addMemoryContainer(memoriesParent, mem);
    }

    public void addMemoryContainer(LinearLayout parent, LinearLayout memory) {

        LinearLayout layParent = new LinearLayout(this);
        layParent.setOrientation(LinearLayout.VERTICAL);
        layParent.setPadding(50, 0, 0, 0);

        //Username + Photo
        LinearLayout layProfile = new LinearLayout(this);
        layProfile.setOrientation(LinearLayout.HORIZONTAL);
        ImageView profileImage = new ImageView(this);

        new GoogleProfilePictureAsync(profileImage, LoginActivity.GOOGLE_ACCOUNT.getPhotoUrl()).execute();

        profileImage.setPadding(0, 0, 50, 0);
        profileImage.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        profileImage.requestLayout();
        layProfile.addView(profileImage);

        TextView usrTxt = new TextView(this);
        usrTxt.setText(LoginActivity.GOOGLE_ACCOUNT.getDisplayName());
        usrTxt.setTextSize(16);
        usrTxt.setTypeface(null, Typeface.BOLD);
        layProfile.addView(usrTxt);
        layParent.addView(layProfile);
        layProfile.setPadding(0, 0, 0, 40);

        //memory
        memory.setPadding(110, 0, 0, 40);
        layParent.addView(memory);

        //City + date + like
        LinearLayout layInfo = new LinearLayout(this);
        layInfo.setPadding(110, 0, 40, 40);
        layInfo.setOrientation(LinearLayout.HORIZONTAL);

        TextView cityTxt = new TextView(this);
        cityTxt.setText("Geneva");
        cityTxt.setTextSize(14);
        cityTxt.setPadding(0, 0, 40, 20);
        layInfo.addView(cityTxt);

        TextView timeTxt = new TextView(this);
        timeTxt.setText("29.01.1996");
        layInfo.addView(timeTxt);

        layParent.addView(layInfo);
        //Comments

        parent.addView(layParent);

    }


    public void addNewMemory(View view) {
        Intent intent = new Intent(this, CreateMemoryActivity.class);
        startActivity(intent);
    }

    public void goToPublicMemory(View view) {

    }

    public void goToMap(View view) {

    }

    public void goToNotification(View view) {

    }

    public void goToUser(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }


}
