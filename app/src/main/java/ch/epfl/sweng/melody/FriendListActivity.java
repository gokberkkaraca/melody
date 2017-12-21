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
import ch.epfl.sweng.melody.util.NavigationHandler;

import static ch.epfl.sweng.melody.MainActivity.getFirebaseAuthInstance;
import static ch.epfl.sweng.melody.PublicMemoryActivity.EXTRA_GOING_TO_USER_LIST;

public class FriendListActivity extends AppCompatActivity {

    DividerItemDecoration dividerItemDecoration;
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private List<UserContactInfo> allFriends;
    private List<UserContactInfo> friendsToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Intent intent = getIntent();
        String userList = intent.getStringExtra(EXTRA_GOING_TO_USER_LIST);

        //--------------------
        MainActivity.setUser(new User(getFirebaseAuthInstance().getCurrentUser()));
//        DatabaseHandler.getUser(MainActivity.getUser().getId(), new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                MainActivity.setUser(dataSnapshot.getValue(User.class));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //------------------

        switch (userList) {
            case "requests":
                allFriends = null;
                friendsToDisplay = MainActivity.getUser().getFriendshipListRequests();
                ((TextView) findViewById(R.id.friends_toolbar_title)).setText(R.string.my_friends_requests);
                break;
            case "friends":
                allFriends = MainActivity.getUser().getListFriends();
                friendsToDisplay = MainActivity.getUser().getListFriends();
                break;
            default:
                allFriends = new ArrayList<>();
                friendsToDisplay = new ArrayList<>();
                DatabaseHandler.getAllUsers(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                            User user = userDataSnapshot.getValue(User.class);
                            assert user != null;
                            friendsToDisplay.add(user.getUserContactInfo());
                            allFriends.add(user.getUserContactInfo());
                        }

                        friendAdapter = new FriendAdapter(friendsToDisplay);
                        friendsRecyclerView.setAdapter(friendAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }

        setTitle("");
        Toolbar friendToolbar = findViewById(R.id.friends_toolbar);
        setSupportActionBar(friendToolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        friendsRecyclerView = findViewById(R.id.friends_recyclerView);

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

    private void filter(String text) {

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
