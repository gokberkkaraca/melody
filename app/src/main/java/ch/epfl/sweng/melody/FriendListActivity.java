package ch.epfl.sweng.melody;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import ch.epfl.sweng.melody.user.FriendAdapter;
import ch.epfl.sweng.melody.user.UserContactInfo;

public class FriendListActivity extends AppCompatActivity {
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    DividerItemDecoration dividerItemDecoration;

    private List<UserContactInfo> friendsToDisplay = MainActivity.getUser().getFriends();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

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
