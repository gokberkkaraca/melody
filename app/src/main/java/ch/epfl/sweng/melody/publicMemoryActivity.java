package ch.epfl.sweng.melody;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.melody.memory.Memory;

public class publicMemoryActivity extends AppCompatActivity {

    RelativeLayout menuLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_public_memory);

        menuLayout = (RelativeLayout)findViewById(R.id.menu);
       // toolbar = (android.support.v7.widget.LinearLayout) findViewById(R.id.menu);
        //setSupportActionBar(toolbar);

        final LinearLayout memoryContainer = (LinearLayout) findViewById(R.id.memoryContainer);

        ArrayList<Memory> memories = new ArrayList<Memory>();
        Memory mem1 = new Memory("hello,");
        Memory mem2 = new Memory("world!");
        memories.add(mem1);
        memories.add(mem2);

        for(int i=0; i<memories.size(); i++){
            //Create TextView for text
            TextView text = new TextView(this);
            text.setText(memories.get(i).getText());
            memoryContainer.addView(text);

            //Create ImageView for photos
            ImageView photos = new ImageView(this);
            memoryContainer.addView(photos);

        }


    }

    public void addNewMemory (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToPublicMemory (View view){

    }

    public void goToMap (View view){

    }

    public void goToNotification (View view){

    }

    public void goToUser (View view){

    }


}
