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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.FriendAdapter;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;

import static ch.epfl.sweng.melody.PublicMemoryActivity.EXTRA_GOINGTOREQUESTS;

public class FriendListActivity extends AppCompatActivity {

    DividerItemDecoration dividerItemDecoration;
    private RecyclerView usersRecyclerView;
    private FriendAdapter userAdapter;
    private List<UserContactInfo> allUsers;
    private List<UserContactInfo> usersToDisplay;
    private boolean isFriends = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Intent intent = getIntent();
        String isRequestsExtra = intent.getStringExtra(EXTRA_GOINGTOREQUESTS);
        if (isRequestsExtra == null) isRequestsExtra = "false";
        Boolean isRequests = Boolean.valueOf(isRequestsExtra);

        if (isRequests) {
            usersToDisplay = MainActivity.getUser().getFriendshipListRequests();
            ((TextView) findViewById(R.id.friends_toolbar_title)).setText(R.string.my_friends_requests);
        } else if (isFriends) {
            usersToDisplay = MainActivity.getUser().getListFriends();
        }

        setTitle("");
        Toolbar friendToolbar = findViewById(R.id.friends_toolbar);
        setSupportActionBar(friendToolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        usersRecyclerView = findViewById(R.id.friends_recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        usersRecyclerView.setLayoutManager(mLayoutManager);

        dividerItemDecoration = new DividerItemDecoration(usersRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        usersRecyclerView.addItemDecoration(dividerItemDecoration);

        userAdapter = new FriendAdapter(usersToDisplay);
        usersRecyclerView.setAdapter(userAdapter);

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

        System.out.println(allUsers);
        usersToDisplay.clear();
        for (UserContactInfo user : allUsers) {
            if (user.getDisplayName().contains(text))
                usersToDisplay.add(user);
            else if (user.getEmail().contains(text))
                usersToDisplay.add(user);
        }
        userAdapter.notifyDataSetChanged();
    }
}
