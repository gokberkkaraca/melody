package ch.epfl.sweng.melody;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.user.User;

public class PublicMemoryActivity extends Activity {

    private static User user;
    private List<Memory> memoryList;
    private RecyclerView recyclerView;
    private MemoryAdapter memoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_memory);

        memoryList = new ArrayList<>();
        fetchMemoriesFromDatabase();

        user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_INFO);
    }

    private void fetchMemoriesFromDatabase() {
        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;
                    memoryList.add(memory);
                }

                memoryAdapter = new MemoryAdapter(memoryList);
                memoryAdapter.notifyDataSetChanged();

                recyclerView = (RecyclerView) findViewById(R.id.memories_recyclerview);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(PublicMemoryActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(memoryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addNewMemory(View view) {
        Intent intent = new Intent(this, CreateMemoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
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
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
