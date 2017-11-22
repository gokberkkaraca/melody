package ch.epfl.sweng.melody;

import android.Manifest;
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
import android.support.v4.app.FragmentActivity;
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
import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.util.DialogUtils;
import ch.epfl.sweng.melody.util.MenuButtons;
import ch.epfl.sweng.melody.util.PermissionUtils;

import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_GPS;
import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_LOCATION;
import static ch.epfl.sweng.melody.util.PermissionUtils.locationManager;

public class ShowMapActivity extends FragmentActivity
        implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMapClickListener {
    private int filterRadius = 0;
    private SerializableLocation currentLocation = new SerializableLocation(0, 0, "FAKE");
    private SerializableLocation pickLocation;
    private GoogleMap mMap;
    private Marker currentMarker;
    private Marker pickPlaceMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PermissionUtils.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        PermissionUtils.accessLocationWithPermission(this, this);
        filterByLocationSeekBar();
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
    public void onLocationChanged(Location location) {
        updateMarkerOfCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            DialogUtils.showGPSDisabledDialog(this);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        if (pickPlaceMarker != null) {
            pickPlaceMarker.setPosition(point);
            pickLocation = new SerializableLocation(point.latitude, point.longitude, "PICK");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));
        }
    }

    public void filterByLocationSeekBar() {
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
                if (pickPlaceMarker != null) {
                    addMarkerForPickedLocation();
                    filerMemoriesByLocation(radiusValue, pickLocation);
                } else {
                    addMarkerForCurrentLocation();
                    filerMemoriesByLocation(radiusValue, currentLocation);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void pickLocation(View view) {
        mMap.setOnMapClickListener(this);
        currentMarker.remove();
        pickPlaceMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
    }

    public void findMemoryAroundCurrentLocation(View view) {
        if(pickPlaceMarker!=null) {
            pickPlaceMarker.remove();
            pickPlaceMarker = null;
            pickLocation = null;
        }
        addMarkerForCurrentLocation();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12.0f));
    }

    private void updateMarkerOfCurrentLocation(Location location) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String addressText = "";
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            addressText = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (currentMarker == null) {
            currentLocation = new SerializableLocation(location.getLatitude(), location.getLongitude(), addressText);
            addMarkerForCurrentLocation();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12.0f));
        } else {
            currentLocation = new SerializableLocation(location.getLatitude(), location.getLongitude(), addressText);
            currentMarker.setPosition(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        }
    }


    private void addMarkerForCurrentLocation() {
        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .title("Your location").
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    private void addMarkerForPickedLocation() {
        pickPlaceMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(pickLocation.getLatitude(), pickLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
    }

    private void filerMemoriesByLocation(TextView radiusValue, SerializableLocation location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        radiusValue.setText(getString(R.string.showRadiusMessage, filterRadius));
        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;
                    SerializableLocation location = memory.getSerializableLocation();
                    if (location.distanceTo(location) < filterRadius * 1000) {
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

}
