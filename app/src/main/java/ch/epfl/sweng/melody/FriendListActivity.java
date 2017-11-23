package ch.epfl.sweng.melody;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.user.FriendAdapter;
import ch.epfl.sweng.melody.user.UserContactInfo;

public class FriendListActivity extends AppCompatActivity {
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    DividerItemDecoration dividerItemDecoration;

    //private List<UserContactInfo> friendsToDisplay = MainActivity.getUser().getFriends(); //REAL LIST
    private List<UserContactInfo> friendsToDisplay = new ArrayList<>(); //MOCK LIST

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //----------------Mock datas-----------------------
        friendsToDisplay.clear();
        UserContactInfo user1 = new UserContactInfo("user1", "First Friend :)", "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/resources%2F1511445418787.jpg?alt=media&token=79ef569d-b65a-47b6-b1b9-3b32098153ff", "first@gmail.com");
        UserContactInfo user2 = new UserContactInfo("user2", "Second ", "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/resources%2F1511391615137.jpg?alt=media&token=d1b06dcb-6786-4717-89b5-4775e3d92112", "second@gmail.com");
        friendsToDisplay.add(user1);
        friendsToDisplay.add(user2);
        //-------------------------------------------------

        setTitle("");
        Toolbar friendToolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(friendToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        friendsRecyclerView = (RecyclerView) findViewById(R.id.friends_recyclerView);

        friendsRecyclerView.setHasFixedSize(true); //improve performance but keep it ?

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        friendsRecyclerView.setLayoutManager(mLayoutManager);

        dividerItemDecoration = new DividerItemDecoration(friendsRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        friendsRecyclerView.addItemDecoration(dividerItemDecoration);

        friendAdapter = new FriendAdapter(friendsToDisplay);
        friendsRecyclerView.setAdapter(friendAdapter);
    }


}
