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
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.melody.database.FirebaseBackgroundService;
import ch.epfl.sweng.melody.location.LocationService;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.NavigationHandler;
import ch.epfl.sweng.melody.util.PermissionUtils;
import ch.epfl.sweng.melody.util.UserPreferences;

import static ch.epfl.sweng.melody.util.FetchingUtils.createMemoriesListener;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_GPS;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_LOCATION;
import static ch.epfl.sweng.melody.util.PermissionUtils.locationManager;

public class PublicMemoryActivity extends AppCompatActivity implements DialogInterface.OnDismissListener { //extended FragmentActivity before

    public static final String EXTRA_GOING_TO_USER_LIST = "ch.epfl.sweng.GOING_TO_USER_LISTS";
    public static boolean insidePublicActivity;
    private static MemoryAdapter memoryAdapter;
    private static long memoryStartTime = 0L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
    private static Calendar calendar;
    private static RecyclerView recyclerView;
    private static Parcelable recyclerViewState;
    private Toolbar myToolbar;
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 8;
    public static LruCache<String, Bitmap> mMemoryCache;

    private List<Memory> memoryList;

    public static ColorCode getColorCode(String colorValue) {

        switch (colorValue) {
            case "1":
                return new ColorCode(User.ThemeColor.RED, R.color.red);
            case "2":
                return new ColorCode(User.ThemeColor.GREEN, R.color.green);
            case "3":
                return new ColorCode(User.ThemeColor.BLUELIGHT, R.color.blueLight);
            case "4":
                return new ColorCode(User.ThemeColor.BLUEDARK, R.color.blueDark);
            case "5":
                return new ColorCode(User.ThemeColor.BLACK, R.color.black);
            default:
                return new ColorCode(User.ThemeColor.RED, R.color.red);
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memoryStartTime = 0L;
        setContentView(R.layout.activity_public_memory);

        insidePublicActivity = true;

        setUserPreference();

        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.menu));

        memoryList = new ArrayList<>();

        memoryAdapter = new MemoryAdapter(memoryList);
        memoryAdapter.notifyDataSetChanged();

        recyclerView = findViewById(R.id.memories_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(memoryAdapter);

        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        if (MainActivity.getUser() != null) {
            startService(new Intent(this, FirebaseBackgroundService.class));
            startService(new Intent(this, LocationService.class));
        }
        PermissionUtils.accessLocationWithPermission(this);
        createMemoriesListener(memoryList, memoryAdapter, memoryStartTime, null);
        memoryAdapter.notifyDataSetChanged();

        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) { //caching the video thumbnail to not recompute them again
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    private void setUserPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        User user = MainActivity.getUser();
        String colorValue = sharedPref.getString("themeColor", "RED");

        myToolbar = findViewById(R.id.public_toolbar);
        myToolbar.setTitle("Melody");

        if (user != null) {
            ColorCode colorCode = getColorCode(colorValue);
            UserPreferences.colorThemeValue = colorCode.getThemeColorValue();
            user.setThemeColor(colorCode.getThemeColor());
            myToolbar.setTitleTextColor(ContextCompat.getColor(this, colorCode.getThemeColorValue()));

            int rMin = Integer.parseInt(sharedPref.getString("minRadius", "1"));
            user.setMinRadius(rMin);
            int rMax = Integer.parseInt(sharedPref.getString("maxRadius", "100"));
            user.setMaxRadius(rMax);
            boolean notificationsOn = sharedPref.getBoolean("notifications", true);
            user.setNotificationsOn(sharedPref.getBoolean("notifications", notificationsOn));
        }
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

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (calendar != null) {
            setTitle("Melody - " + dateFormat.format(calendar.getTime()));
            recyclerView.removeAllViews();
            memoryList.clear();
            memoryAdapter.notifyDataSetChanged();
            createMemoriesListener(memoryList, memoryAdapter, memoryStartTime, null);
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
        NavigationHandler.goToCreateMemoryActivity(this);
    }

    public void goToPublicMemoryActivity(View view) {
        NavigationHandler.goToPublicMemoryActivity(this);
    }

    public void goToMapActivity(View view) {
        NavigationHandler.goToMapActivity(this);
    }

    public void goToNotification(View view) {
        NavigationHandler.goToFriendListActivity(this, "requests");
    }

    public void goToUser(View view) {
        NavigationHandler.goToUserProfileActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time_changing_item:
                showDatePickerDialog();
                return true;

            case R.id.user_search_item:
                NavigationHandler.goToFriendListActivity(this, "all");
                return true;

            case R.id.see_friends_item:
                NavigationHandler.goToFriendListActivity(this, "friends");
                return true;

            case R.id.settings_item:
                NavigationHandler.goToSettingsActivity(this);
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

        @NonNull
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

    public static class ColorCode {

        private User.ThemeColor themeColor;
        private int themeColorValue;

        ColorCode(User.ThemeColor themeColor, int themeColorValue) {
            this.themeColor = themeColor;
            this.themeColorValue = themeColorValue;
        }

        protected User.ThemeColor getThemeColor() {
            return themeColor;
        }

        protected int getThemeColorValue() {
            return themeColorValue;
        }
    }
}
