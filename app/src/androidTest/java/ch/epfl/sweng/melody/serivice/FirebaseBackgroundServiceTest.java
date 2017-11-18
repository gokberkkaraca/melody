package ch.epfl.sweng.melody.serivice;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.service.FirebaseBackgroundService;

import static org.junit.Assert.assertTrue;

/**
 * Created by maxwell on 02.11.17.
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseBackgroundServiceTest {
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void onBind() throws Exception {
        final FirebaseBackgroundService firebaseBackgroundService = new FirebaseBackgroundService();
        context.startService(new Intent(context, FirebaseBackgroundService.class));

        firebaseBackgroundService.onBind(new Intent());
    }

    @Test
    public void onCreate() throws Exception {
        assertTrue(FirebaseBackgroundService.isServiceStarted());
    }

    @Test
    public void onDestory() throws Exception{
        context.stopService(new Intent(context,FirebaseBackgroundService.class));
        assertTrue(!FirebaseBackgroundService.isServiceStarted());
    }
}