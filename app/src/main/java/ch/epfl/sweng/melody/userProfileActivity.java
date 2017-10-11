package ch.epfl.sweng.melody;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class userProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        LinearLayout memoriesParent = (LinearLayout) findViewById(R.id.showMemoriesLayout);

        LinearLayout mem = new LinearLayout(this);
        TextView txt = new TextView(this);
        txt.setText("Cet endroit est génial");
        //memoriesParent.addView(txt);
        mem.addView(txt);
        addMemoryContainer(memoriesParent,mem);


    }

    void goToEdit(View view) {}

    public void addMemoryContainer(LinearLayout parent, LinearLayout memory){

        LinearLayout layParent = new LinearLayout(this);
        layParent.setOrientation(LinearLayout.VERTICAL);
        layParent.setPadding(50,0,0,0);

        //Username + Photo
        LinearLayout layProfile = new LinearLayout(this);
        layProfile.setOrientation(LinearLayout.HORIZONTAL);
        ImageView profileImage = new ImageView(this);
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








