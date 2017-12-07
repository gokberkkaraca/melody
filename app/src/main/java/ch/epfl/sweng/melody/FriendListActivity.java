package ch.epfl.sweng.melody;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.melody.user.FriendAdapter;
import ch.epfl.sweng.melody.user.UserContactInfo;

import static ch.epfl.sweng.melody.PublicMemoryActivity.EXTRA_GOINGTOREQUESTS;

public class FriendListActivity extends AppCompatActivity {

    DividerItemDecoration dividerItemDecoration;
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private List<UserContactInfo> allFriends = MainActivity.getUser().getListFriends();
    private List<UserContactInfo> friendsToDisplay = MainActivity.getUser().getListFriends();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);


        Intent intent = getIntent();
        String isRequestsExtra = intent.getStringExtra(EXTRA_GOINGTOREQUESTS);
        if (isRequestsExtra == null) isRequestsExtra = "false";
        Boolean isRequests = Boolean.valueOf(isRequestsExtra);

        if (isRequests) {
            friendsToDisplay = MainActivity.getUser().getFriendshipListRequests();
            ((TextView) findViewById(R.id.friends_toolbar_title)).setText(R.string.my_friends_requests);
        } else {
            friendsToDisplay = MainActivity.getUser().getListFriends();
        }

        setTitle("");
        Toolbar friendToolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(friendToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        friendsRecyclerView = (RecyclerView) findViewById(R.id.friends_recyclerView);

        //friendsRecyclerView.setHasFixedSize(true); //improve performance but keep it ?

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        friendsRecyclerView.setLayoutManager(mLayoutManager);

        dividerItemDecoration = new DividerItemDecoration(friendsRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        friendsRecyclerView.addItemDecoration(dividerItemDecoration);

        friendAdapter = new FriendAdapter(friendsToDisplay);
        friendsRecyclerView.setAdapter(friendAdapter);

        SearchView simpleSearchView = findViewById(R.id.search_view);

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    public void filter(String text) {

        friendsToDisplay.clear();
        for (UserContactInfo friend : allFriends) {
            if (friend.getDisplayName().contains(text))
                friendsToDisplay.add(friend);
            else if (friend.getEmail().contains(text))
                friendsToDisplay.add(friend);
        }
        friendAdapter.notifyDataSetChanged();
    }
}
