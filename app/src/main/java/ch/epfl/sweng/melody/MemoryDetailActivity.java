package ch.epfl.sweng.melody;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;

public class MemoryDetailActivity extends AppCompatActivity {
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_detail);
        fetchMemoryFromDatabase(getIntent().getStringExtra("memoryId"));
    }

    private void fetchMemoryFromDatabase(final String memoryId) {
        DatabaseHandler.getMemory(memoryId, new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memory = dataSnapshot.getValue(Memory.class);

                TextView date = (TextView) findViewById(R.id.memoryDate);
                date.setText(memory.getTime().toString());

                TextView location = (TextView) findViewById(R.id.memoryLocation);
                location.setText(memory.getLocation());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed!");
            }
        });
    }
}
