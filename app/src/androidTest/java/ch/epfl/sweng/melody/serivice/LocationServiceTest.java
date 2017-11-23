package ch.epfl.sweng.melody.serivice;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.sweng.melody.location.LocationService;

import static org.junit.Assert.assertTrue;

/**
 * Created by maxwell on 22.11.17.
 */

public class LocationServiceTest {
    private Context context;
    private Intent intent;
    private LocationService locationService;
    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        locationService = new LocationService();
        intent = new Intent(context,LocationService.class);
    }

    @Test
    public void onBind() throws Exception {
        context.startService(intent);
        locationService.onBind(new Intent());
    }

    @Test
    public void onCreate() throws Exception {
        assertTrue(LocationService.isServiceStarted());
    }

    @Test @Ignore
    public void onDestory() throws Exception{
        context.stopService(intent);
        assertTrue(!LocationService.isServiceStarted());
    }
}
