package ch.epfl.sweng.melody;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.MenuButtons;
import ch.epfl.sweng.melody.util.PermissionUtils;

import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_GPS;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_LOCATION;
import static ch.epfl.sweng.melody.util.PermissionUtils.locationManager;

public class PublicMemoryActivity extends AppCompatActivity implements DialogInterface.OnDismissListener { //extended FragmentActivity before

    private static MemoryAdapter memoryAdapter;
    private static long memoryStartTime = 0L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
    private static boolean datePicked = false;
    private static Calendar calendar;
    private List<Memory> memoryList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_memory);

        setTitle("Melody");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.public_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.menu));

        memoryList = new ArrayList<>();
        if (MainActivity.getUser() != null) {
            startService(new Intent(this, FirebaseBackgroundService.class));
            startService(new Intent(this, LocationService.class));
        }
        PermissionUtils.accessLocationWithPermission(this);
        fetchMemoriesFromDatabase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GPS: {
                if (PermissionUtils.locationManager == null) {
                    PermissionUtils.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                }

                assert PermissionUtils.locationManager != null;
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    DialogUtils.showGPSDisabledDialog(this);
            }
        }
    }

    private void fetchMemoriesFromDatabase() {
        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;
                    if (memory.getLongId() > memoryStartTime) {
                        memoryList.add(memory);
                    }
                }

                memoryAdapter = new MemoryAdapter(memoryList);
                memoryAdapter.notifyDataSetChanged();

                recyclerView = findViewById(R.id.memories_recyclerview);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
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

    public void showDatePickerDialog() { //deleted View v because was unused
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(calendar != null) {
            setTitle("Melody - " + dateFormat.format(calendar.getTime()));
            recyclerView.removeAllViews();  //good way to do it ? Maybe add conditions to prevent reloading
            memoryList = new ArrayList<>();
            fetchMemoriesFromDatabase();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_LOCATION: {
                    PermissionUtils.accessLocationWithPermission(this);
                }
            }
        } else {
            switch (requestCode) {
                case REQUEST_LOCATION: {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        DialogUtils.showLocationPermissionRationale(this);
                    }
                }
            }
        }
    }


    /*************************************************
     ******************* Menu Buttons ****************
     *************************************************/
    public void goToCreateMemoryActivity(View view) {
        MenuButtons.goToCreateMemoryActivity(this);
    }

    public void goToPublicMemoryActivity(View view) {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    public void goToMapActivity(View view) {
        MenuButtons.goToMapActivity(this);
    }

    public void goToNotification(View view) {
        MenuButtons.goToNotificationActivity(this);
    }

    public void goToUser(View view) {
        MenuButtons.goToUserProfileActivity(this);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            datePicked = true;
            calendar = Calendar.getInstance();
            calendar.set(year, month, day, 0, 0, 0);
            memoryStartTime = Long.MAX_VALUE - calendar.getTimeInMillis() - TimeUnit.DAYS.toMillis(1);
        }

        @Override
        public void onDismiss(final DialogInterface dialog) {
            super.onDismiss(dialog);
            final Activity activity = getActivity();
            if (activity instanceof DialogInterface.OnDismissListener) {
                ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.time_changing_item:
                showDatePickerDialog();
                return true;

            case R.id.see_friends_item :
                intent = new Intent(this, FriendListActivity.class);
                this.startActivity(intent);
                return true;

            case R.id.settings_item :
                intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.public_toolbar_items, menu);
        return true;
    }

}
