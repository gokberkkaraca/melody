package ch.epfl.sweng.melody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import android.widget.LinearLayout;

import ch.epfl.sweng.melody.memory.Memory;

public class publicMemoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_public_memory);


        //setSupportActionBar(toolbar);


        /*final LinearLayout memoryContainer = (LinearLayout) findViewById(R.id.memoryContainer);

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

        }*/
        LinearLayout memoriesParent = (LinearLayout) findViewById(R.id.publicMemoryActivity_LinearLayout_Memories);


        LinearLayout mem = new LinearLayout(this);
        TextView txt = new TextView(this);
        txt.setText("Cet endroit est génial");
        mem.addView(txt);
        addMemoryContainer(memoriesParent,mem);

        LinearLayout memPhoto = new LinearLayout(this);
        ImageView photo = new ImageView(this);
        photo.setImageDrawable(getResources().getDrawable(R.drawable.home_unfilled));
        photo.setPadding(0,0,50,0);
        memPhoto.addView(photo);
        addMemoryContainer(memoriesParent,memPhoto);

        LinearLayout mem2 = new LinearLayout(this);
        TextView txt2 = new TextView(this);
        txt2.setText("Cet endroit est génial");
        mem2.addView(txt2);
        addMemoryContainer(memoriesParent,mem2);

        LinearLayout mem3 = new LinearLayout(this);
        TextView txt3 = new TextView(this);
        txt3.setText("Cet endroit est génial");
        mem3.addView(txt3);
        addMemoryContainer(memoriesParent,mem3);

        LinearLayout mem4 = new LinearLayout(this);
        TextView txt4 = new TextView(this);
        txt4.setText("Cet endroit est génial");
        mem4.addView(txt4);
        addMemoryContainer(memoriesParent,mem4);

        LinearLayout mem5 = new LinearLayout(this);
        TextView txt5 = new TextView(this);
        txt5.setText("Cet endroit est génial");
        mem5.addView(txt5);
        addMemoryContainer(memoriesParent,mem5);


    }

    public void addMemoryContainer(LinearLayout parent, LinearLayout memory){

        LinearLayout layParent = new LinearLayout(this);
        layParent.setOrientation(LinearLayout.VERTICAL);
        layParent.setPadding(50,0,0,0);

        //Username + Photo
        LinearLayout layProfile = new LinearLayout(this);
        layProfile.setOrientation(LinearLayout.HORIZONTAL);
        ImageView profileImage = new ImageView(this);
        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.bell_filled));
        profileImage.setPadding(0,0,50,0);
        layProfile.addView(profileImage);

        TextView usrTxt = new TextView(this);
        usrTxt.setText("anonymous");
        layProfile.addView(usrTxt);
        layParent.addView(layProfile);
        layProfile.setPadding(0,0,0,40);

        //memory
        memory.setPadding(0,0,0,40);
        layParent.addView(memory);

        //City + date + like
        LinearLayout layInfo = new LinearLayout(this);
        layInfo.setOrientation(LinearLayout.HORIZONTAL);

        TextView cityTxt = new TextView(this);
        cityTxt.setText("Geneva");
        cityTxt.setPadding(0,0,40,20);
        layInfo.addView(cityTxt);

        TextView timeTxt = new TextView(this);
        timeTxt.setText("29.01.1996");
        layInfo.addView(timeTxt);

        layParent.addView(layInfo);
        //Comments


        parent.addView(layParent);

    }


    public void addNewMemory (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
