package ch.epfl.sweng.melody;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.location.LocationObserver;
import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.MenuButtons;
import ch.epfl.sweng.melody.util.PermissionUtils;

import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_GPS;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_LOCATION;
import static ch.epfl.sweng.melody.util.PermissionUtils.locationManager;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationObserver {
    private int filterRadius = 0;
    private LatLng currentLatLng = new LatLng(0,0);
    private SerializableLocation currentLocation = new SerializableLocation(currentLatLng.latitude,currentLatLng.longitude,"FAKE");
    private GoogleMap mMap;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PermissionUtils.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //PermissionUtils.accessLocationWithPermission(this, this);
        filterByLocation();
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Lausanne.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void filterByLocation() {
        TextView title = findViewById(R.id.filter_title);
        title.setTextColor(Color.BLACK);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        final TextView radiusValue = findViewById(R.id.filter_message);
        radiusValue.setText(R.string.ChooseRadius);
        radiusValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        radiusValue.setTextColor(Color.BLACK);

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(100);
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

        thumb.setIntrinsicHeight(50);
        thumb.setIntrinsicWidth(50);
        seekBar.setThumb(thumb);
        seekBar.setProgress(1);
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setPadding(50, 30, 50, 0);
        filterRadius = seekBar.getProgress();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                filterRadius = progressValue;
                mMap.clear();
                currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));
                radiusValue.setText(getString(R.string.showRadiusMessage, filterRadius));
                DatabaseHandler.getAllMemories(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                            Memory memory = memDataSnapshot.getValue(Memory.class);
                            assert memory != null;
                            SerializableLocation location = memory.getSerializableLocation();
                            if (location.distanceTo(currentLocation) < filterRadius * 1000) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title(memory.getText()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            switch (requestCode) {
                case REQUEST_LOCATION: {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        DialogUtils.showLocationPermissionRationale(this);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GPS: {
                if (locationManager == null) {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                }
                assert locationManager != null;
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    DialogUtils.showGPSDisabledDialog(this);
            }
        }
    }


    @Override
    public void update(Location location) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String addressText = "";
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            addressText = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(currentLatLng.longitude==0&&currentLatLng.latitude==0){
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocation = new SerializableLocation(location.getLatitude(), location.getLongitude(), addressText);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));
        }else {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocation = new SerializableLocation(location.getLatitude(), location.getLongitude(), addressText);
        }
        if(currentMarker!=null){
            currentMarker.setPosition(currentLatLng);
        }else{
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }
}
