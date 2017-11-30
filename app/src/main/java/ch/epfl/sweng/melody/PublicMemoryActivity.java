package ch.epfl.sweng.melody;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.MenuButtons;
import ch.epfl.sweng.melody.util.PermissionUtils;

import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_GPS;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_LOCATION;
import static ch.epfl.sweng.melody.util.PermissionUtils.locationManager;

public class PublicMemoryActivity extends AppCompatActivity implements DialogInterface.OnDismissListener { //extended FragmentActivity before

    public static final String EXTRA_GOINGTOREQUESTS = "ch.epfl.sweng.GOINGTOREQUESTS";
    public static LruCache<String, Bitmap> mMemoryCache;
    private static MemoryAdapter memoryAdapter;
    private static long memoryStartTime = 0L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
    private static boolean datePicked = false;
    private static Calendar calendar;
    private static RecyclerView recyclerView;
    private static Parcelable recyclerViewState;
    private static RecyclerView.LayoutManager mLayoutManager;
    private static User user;

    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 8;
    private List<Memory> memoryList;

    public static void refreshPublicLayout() {
        memoryAdapter.notifyDataSetChanged();
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void saveRecyclerViewPosition() {
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    public static void scrollViewToTop() {
        mLayoutManager.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_memory);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        user = MainActivity.getUser();
        String colorValue = sharedPref.getString("themeColor", "RED");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.public_toolbar);
        myToolbar.setTitle("Melody");

        switch (colorValue) {
            case "1":
                user.setThemeColor(User.ThemeColor.RED);
                myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.red));
                break;
            case "2":
                user.setThemeColor(User.ThemeColor.GREEN);
                myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case "3":
                user.setThemeColor(User.ThemeColor.BLUELIGHT);
                myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.blueLight));
                break;
            case "4":
                user.setThemeColor(User.ThemeColor.BLUEDARK);
                myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.blueDark));
                break;
            case "5":
                user.setThemeColor(User.ThemeColor.BLACK);
                myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            default:
                user.setThemeColor(User.ThemeColor.RED);
                myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.red));
        }
        int rMin = Integer.parseInt(sharedPref.getString("minRadius", "1"));
        user.setMinRadius(rMin);
        int rMax = Integer.parseInt(sharedPref.getString("maxRadius", "100"));
        user.setMaxRadius(rMax);
        boolean notificationsOn = sharedPref.getBoolean("notifications", true);
        user.setNotificationsOn(sharedPref.getBoolean("notifications", notificationsOn));

        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.menu));

        memoryList = new ArrayList<>();

        memoryAdapter = new MemoryAdapter(memoryList);
        memoryAdapter.notifyDataSetChanged();

        recyclerView = findViewById(R.id.memories_recyclerview);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(memoryAdapter);

        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        if (MainActivity.getUser() != null) {
            startService(new Intent(this, FirebaseBackgroundService.class));
            startService(new Intent(this, LocationService.class));
        }
        PermissionUtils.accessLocationWithPermission(this);
        fetchMemoriesFromDatabase();

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) { //caching the video thumbnail to not recompute them again
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

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

    private boolean isFriendsMemory(String memoryAuthorId) {
        Map<String, UserContactInfo> Friends = user.getFriends();
        for (UserContactInfo friend : Friends.values()) {
            if(friend.getUserId() == memoryAuthorId)
                return true;
        }
        return false;
    }


    private void fetchMemoriesFromDatabase() {
        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;

                    if (memory.getLongId() > memoryStartTime) {
                        if (isNewMemory(memory.getId())) {
                            if(memory.getPrivacy() == Memory.Privacy.PUBLIC){
                                memoryList.add(memory);
                                memoryAdapter.notifyDataSetChanged();
                            }

                            else if(memory.getPrivacy() == Memory.Privacy.SHARED && isFriendsMemory(memory.getUser().getId())){
                                memoryList.add(memory);
                                memoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean isNewMemory(String memoryId) {
        for (Memory m : memoryList) {
            if (memoryId.equals(m.getId()))
                return false;
        }
        return true;
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
        if (calendar != null) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.time_changing_item:
                showDatePickerDialog();
                return true;

            case R.id.see_friends_item:
                intent = new Intent(this, FriendListActivity.class);
                intent.putExtra(EXTRA_GOINGTOREQUESTS, "false");
                this.startActivity(intent);
                return true;

            case R.id.friends_requests_item:
                intent = new Intent(this, FriendListActivity.class);
                intent.putExtra(EXTRA_GOINGTOREQUESTS, "true");
                this.startActivity(intent);
                return true;

            case R.id.settings_item:
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

}
