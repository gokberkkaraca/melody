package ch.epfl.sweng.melody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FakeMemoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_memory);
        Intent intent = getIntent();
        String text = intent.getStringExtra(CreateMemoryActivity.SEND_TEXT_MESSAGE);
//        byte[] picture_byte = intent.getByteArrayExtra(CreateMemoryActivity.SEND_PHOTO_MESSAGE);
//        Bitmap picture = BitmapFactory.decodeByteArray(picture_byte,0,picture_byte.length);
        Bitmap picture = intent.getParcelableExtra(CreateMemoryActivity.SEND_PHOTO_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.textView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        textView.setText(text);
        imageView.setImageBitmap(picture);
    }
}
