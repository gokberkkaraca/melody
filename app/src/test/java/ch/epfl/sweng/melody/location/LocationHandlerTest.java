package ch.epfl.sweng.melody.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.sweng.melody.CreateMemoryActivity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationHandlerTest {

    private LocationHandler locationHandlerGPSOn;
    private LocationHandler locationHandlerGPSOff;
    private Location location;

    @Before
    public void setUp(){

        // GPS Off
        LocationManager locationManagerGpsOff = mock(LocationManager.class);
        when(locationManagerGpsOff.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);

        CreateMemoryActivity createMemoryActivityGpsOff = mock(CreateMemoryActivity.class);
        when(createMemoryActivityGpsOff.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManagerGpsOff);
        locationHandlerGPSOff = new LocationHandler(createMemoryActivityGpsOff);

        // GPS On
        LocationManager locationManagerGPSOn = mock(LocationManager.class);
        when(locationManagerGPSOn.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);

        CreateMemoryActivity createMemoryActivityGPSOn = mock(CreateMemoryActivity.class);
        when(createMemoryActivityGPSOn.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManagerGPSOn);
        locationHandlerGPSOn = new LocationHandler(createMemoryActivityGPSOn);
        location = mock(Location.class);
    }

    @Test
    public void onLocationChanged() throws Exception {
        locationHandlerGPSOff.onLocationChanged(location);
        locationHandlerGPSOn.onLocationChanged(location);
    }

    @Test
    public void onStatusChanged() throws Exception {
        locationHandlerGPSOff.onStatusChanged(LocationManager.GPS_PROVIDER, 1, new Bundle());
        locationHandlerGPSOn.onStatusChanged(LocationManager.GPS_PROVIDER, 1, new Bundle());
    }

    @Test
    public void onProviderEnabled() throws Exception {
        locationHandlerGPSOff.onProviderEnabled(LocationManager.GPS_PROVIDER);
        locationHandlerGPSOn.onProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Ignore @Test
    public void onProviderDisabled() throws Exception {
        locationHandlerGPSOff.onProviderDisabled(LocationManager.GPS_PROVIDER);
        locationHandlerGPSOn.onProviderDisabled(LocationManager.GPS_PROVIDER);
    }

    @Ignore @Test
    public void showGPSDisabledDialog() throws Exception {
        locationHandlerGPSOff.showGPSDisabledDialog();
        locationHandlerGPSOn.showGPSDisabledDialog();
    }

}