package ch.epfl.sweng.melody;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryPhoto;
import ch.epfl.sweng.melody.user.User;

public class PublicMemoryActivity extends Activity {

    private List<Memory> memoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoriesAdapter memoriesAdapter;

    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_public_memory);

        recyclerView = (RecyclerView) findViewById(R.id.memories_recyclerview);

        memoriesAdapter = new MemoriesAdapter(memoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(memoriesAdapter);

        /*
        user = (User) getIntent().getExtras().getSerializable("USER");

        addTextMemory("my memory 1");
        addTextMemory("my memory 2");
        addTextMemory("my memory 3");
        addTextMemory("my memory 4");
        */

        createSomeData();

    }

    private void createSomeData() {
        Memory memory = new Memory("123456", "user1", "Some Text");
        memoryList.add(memory);
        memory = new Memory("134546", "user2", "Some more Text");
        memoryList.add(memory);
        memory = new Memory("123646", "user3", "A lot more Text blablabla");
        memoryList.add(memory);
        memory = new Memory("134546", "user2", "Some more Text");
        memoryList.add(memory);
        memory = new Memory("123646", "user3", "A lot more Text blablabla");
        memoryList.add(memory);
        memory = new Memory("134546", "user2", "Some more Text");
        memoryList.add(memory);
        memory = new Memory("123646", "user3", "A lot more Text blablabla");
        memoryList.add(memory);
        memory = new Memory("134546", "user2", "Some more Text");
        memoryList.add(memory);
        memory = new Memory("123646", "user3", "A lot more Text blablabla");
        memoryList.add(memory);

        memoriesAdapter.notifyDataSetChanged();

        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    MemoryPhoto mem = memDataSnapshot.getValue(MemoryPhoto.class);
                    addPhotoMemory(mem);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

    }

    public void addPhotoMemory(MemoryPhoto mem) {
      /*  LinearLayout memoriesParent = (LinearLayout) findViewById(R.id.publicMemoryActivity_LinearLayout_Memories);
        LinearLayout memPhoto = new LinearLayout(this);

        //Photos
        ImageView img = new ImageView(this);

        new GoogleProfilePictureAsync(img,Uri.parse(mem.getPhotos().get(0))).execute();
        memPhoto.addView(img);

        addMemoryContainer(memoriesParent, memPhoto,mem);*/
    }

    /*public void addTextMemory(String txt) {
        LinearLayout memoriesParent = (LinearLayout) findViewById(R.id.publicMemoryActivity_LinearLayout_Memories);
        LinearLayout mem = new LinearLayout(this);
        TextView txtMem = new TextView(this);
        txtMem.setText(txt);
        mem.addView(txtMem);
        addMemoryContainer(memoriesParent, mem);
    }*/

    public void addMemoryContainer(LinearLayout parent, LinearLayout memory,Memory mem) {

        LinearLayout layParent = new LinearLayout(this);
        layParent.setOrientation(LinearLayout.VERTICAL);
        layParent.setPadding(50, 0, 0, 0);

        //Username + Photo
        LinearLayout layProfile = new LinearLayout(this);
        layProfile.setOrientation(LinearLayout.HORIZONTAL);
        ImageView profileImage = new ImageView(this);

        new GoogleProfilePictureAsync(profileImage, Uri.parse(user.getProfilePhotoUrl())).execute();

        profileImage.setPadding(0, 0, 50, 0);
        profileImage.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        profileImage.requestLayout();
        layProfile.addView(profileImage);

        TextView usrTxt = new TextView(this);
        usrTxt.setText(user.getDisplayName());
        usrTxt.setTextSize(16);
        usrTxt.setTypeface(null, Typeface.BOLD);
        layProfile.addView(usrTxt);
        layParent.addView(layProfile);
        layProfile.setPadding(0, 0, 0, 40);

        //memory
        memory.setPadding(110, 0, 0, 40);
        layParent.addView(memory);

        //Text
        TextView txtMem = new TextView(this);
        txtMem.setText(mem.getText());
        txtMem.setPadding(110, 0, 40, 40);
        layParent.addView(txtMem);

        //Date + City
        LinearLayout layInfo = new LinearLayout(this);
        layInfo.setPadding(110, 0, 40, 40);
        layInfo.setOrientation(LinearLayout.HORIZONTAL);

        TextView cityTxt = new TextView(this);
        cityTxt.setText(mem.getLocation());
        cityTxt.setTextSize(14);
        cityTxt.setPadding(0, 0, 40, 20);
        layInfo.addView(cityTxt);

        TextView timeTxt = new TextView(this);
        timeTxt.setText(mem.getTime().toString());
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
