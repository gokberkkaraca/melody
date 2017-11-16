package ch.epfl.sweng.melody;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ch.epfl.sweng.melody.util.DialogUtils;

import static ch.epfl.sweng.melody.util.PermissionUtils.REQUEST_LOCATION;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {
    private int filterRadius = 0;

    private GoogleMap mMap;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        accessWithPermission(REQUEST_LOCATION);
        filterByLocation();
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
                radiusValue.setText(getString(R.string.showRadiusMessage, filterRadius));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void accessWithPermission(int requestCode) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                else
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
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
    public void onLocationChanged(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Marker in Lansanne"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));
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
}
