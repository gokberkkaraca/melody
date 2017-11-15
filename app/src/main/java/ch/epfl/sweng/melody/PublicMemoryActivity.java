package ch.epfl.sweng.melody;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class PublicMemoryActivity extends Activity {

    private static User user;
    int filterRadius = 0;
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        PublicMemoryActivity.this.finishAffinity();
                    }
                }).create().show();
    }

    public void filterByLocation(View view) {
        LinearLayout radiusView = new LinearLayout(this);
        radiusView.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(10, 70, 10, 0);

        AlertDialog.Builder radiusDialog = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams titleLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayoutParam.setMargins(0, 20, 0, 0);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 30, 0, 0);
        title.setLayoutParams(titleLayoutParam);
        title.setText(R.string.radiusTitle);
        radiusDialog.setCustomTitle(title);

        final TextView radiusValue = new TextView(this);
        radiusValue.setId(R.id.radiusValue);
        radiusValue.setLayoutParams(params);
        radiusValue.setText(R.string.ChooseRadius);
        radiusValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        radiusValue.setTextColor(Color.BLACK);

        SeekBar seekBar = new SeekBar(this);
        seekBar.setId(R.id.rediusSeekBar);

        seekBar.setLayoutParams(params);
        seekBar.setMax(100);
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

        thumb.setIntrinsicHeight(50);
        thumb.setIntrinsicWidth(50);
        seekBar.setThumb(thumb);
        seekBar.setProgress(1);
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setPadding(50, 30, 50, 0);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        seekBar.setLayoutParams(layoutParams);
        filterRadius = seekBar.getProgress();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                filterRadius = progressValue;
                radiusValue.setText(getString(R.string.showRadiusMessage, filterRadius));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        radiusView.addView(seekBar);
        radiusView.addView(radiusValue);
        radiusDialog.setView(radiusView);
        AlertDialog dialog = radiusDialog.create();
        dialog.show();
    }

    /*************************************************
     ******************* Menu Buttons ****************
     *************************************************/
    public void goToCreateMemoryActivity(View view) {
        MenuButtons.goToCreateMemoryActivity(this, user);
    }

    public void goToPublicMemoryActivity(View view) {
        MenuButtons.goToPublicMemoryActivity(this, user);
    }

    public void goToMapActivity(View view) {
        MenuButtons.goToMapActivity(this, user);
    }

    public void goToNotification(View view) {
        MenuButtons.goToNotificationActivity(this, user);
    }

    public void goToUser(View view) {
        MenuButtons.goToUserProfileActivity(this, user);
    }
}
