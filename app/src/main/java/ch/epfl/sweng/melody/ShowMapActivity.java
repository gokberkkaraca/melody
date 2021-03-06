package ch.epfl.sweng.melody;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.location.LocationListenerSubject;
import ch.epfl.sweng.melody.location.LocationObserver;
import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.NavigationHandler;
import ch.epfl.sweng.melody.util.UserPreferences;

public class ShowMapActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        LocationObserver,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapLongClickListener {
    private int filterRadius = 0;
    private SerializableLocation currentLocation = new SerializableLocation(0, 0, "Current");
    private SerializableLocation filterOrigin = new SerializableLocation(0, 0, "origin point");
    private SerializableLocation markerLocation = new SerializableLocation(0, 0, "Marker");
    private GoogleMap mMap;
    private int colorThemeValue;
    private boolean isfirstTimeToUpdateLocation;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        seekBar = findViewById(R.id.seekBar);
        seekbarConfig();
        LocationListenerSubject.getLocationListenerInstance().registerObserver(this);
        colorThemeValue = UserPreferences.colorThemeValue;
        isfirstTimeToUpdateLocation = true;

    }

    @Override
    public void onBackPressed() {
        NavigationHandler.goToPublicMemoryActivity(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        mMap.setOnMapLongClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12.0f));
        updateLocation(filterOrigin, currentLocation.getLongitude(), currentLocation.getLatitude());
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
        updateLocation(filterOrigin, currentLocation.getLongitude(), currentLocation.getLatitude());
        seekBar.setProgress(0);
        return false;
    }

    @Override
    public void update(Location location) {
        updateLocation(currentLocation, location.getLongitude(), location.getLatitude());
        if (isfirstTimeToUpdateLocation) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12.0f));
            updateLocation(filterOrigin, currentLocation.getLongitude(), currentLocation.getLatitude());
            isfirstTimeToUpdateLocation = false;
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        updateLocation(filterOrigin, latLng.longitude, latLng.latitude);
        updateLocation(markerLocation, latLng.longitude, latLng.latitude);
        seekBar.setProgress(0);
    }

    public void seekbarConfig() {
        User user = MainActivity.getUser();
        TextView title = findViewById(R.id.filter_title);
        title.setTextColor(Color.BLACK);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        final TextView radiusValue = findViewById(R.id.filter_message);
        radiusValue.setText(getString(R.string.showRadiusMessage, user.getMinRadius()));
        radiusValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        radiusValue.setTextColor(Color.BLACK);

        seekBar.setMax(user.getMaxRadius());
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

        thumb.setIntrinsicHeight(50);
        thumb.setIntrinsicWidth(50);
        seekBar.setThumb(thumb);
        seekBar.setProgress(user.getMinRadius());
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setPadding(50, 10, 50, 0);
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        filterRadius = seekBar.getProgress();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                filterRadius = progressValue;
                radiusValue.setText(getString(R.string.showRadiusMessage, filterRadius));
                mMap.clear();
                filerMemoriesByLocation(filterOrigin);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Take a text and resize it with a certain length, and keep the last word in the resized text
     * a completed word.
     */
    private String takeSubtext(String text, int length) {
        if (text.length() > length) {
            String reverse = new StringBuffer(text.substring(0, length)).reverse().toString();
            int i = 0;
            while (i < length && reverse.charAt(0) != ' ') {
                reverse = reverse.substring(1, reverse.length());
                i++;
            }
            return new StringBuffer(reverse).reverse().toString();
        } else {
            return text;
        }
    }

    private void filerMemoriesByLocation(final SerializableLocation location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        if (location.getLongitude() == markerLocation.getLongitude() && location.getLatitude() == markerLocation.getLatitude()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        DatabaseHandler.getAllMemories(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                    Memory memory = memDataSnapshot.getValue(Memory.class);
                    assert memory != null;
                    SerializableLocation memoryLocation = memory.getSerializableLocation();
                    if (memoryLocation.distanceTo(location) < filterRadius * 1000) {
                        final LatLng latLng = new LatLng(memoryLocation.getLatitude(), memoryLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(memory.getId()));

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                marker.showInfoWindow();
                                return false;
                            }
                        });

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(final Marker marker) {
                                final ViewGroup nullParent = null;
                                View v = getLayoutInflater().inflate(R.layout.info_window_layout, nullParent);

                                Memory markerMemory = dataSnapshot.child(marker.getTitle()).getValue(Memory.class);
                                assert markerMemory != null;

                                ((TextView) v.findViewById(R.id.userName)).setText(markerMemory.getUser().getDisplayName());
                                ((TextView) v.findViewById(R.id.uploadTime)).setText(markerMemory.getTime().toString());

                                String text = markerMemory.getText();
                                if (text.length() > 60) {
                                    ((TextView) v.findViewById(R.id.memoryText)).setText(getString(R.string.briefText, takeSubtext(markerMemory.getText(), 100)));
                                } else {
                                    ((TextView) v.findViewById(R.id.memoryText)).setText(text);
                                }

                                if (markerMemory.getPhotoUrl() != null) {
                                    Picasso.with(getApplicationContext()).load(markerMemory.getPhotoUrl()).into((ImageView) v.findViewById(R.id.memoryImage));
                                }
                                return v;
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateLocation(SerializableLocation location, double longitude, double latitude) {
        location.setLongitude(longitude);
        location.setLatitude(latitude);
    }
}
