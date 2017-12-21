package ch.epfl.sweng.melody.serivice;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.database.FirebaseBackgroundService;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FirebaseBackgroundServiceTest {
    private Context context;
    private Intent intent;
    private FirebaseBackgroundService firebaseBackgroundService;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        firebaseBackgroundService = new FirebaseBackgroundService();
        intent = new Intent(context, FirebaseBackgroundService.class);
    }

    @Test
    public void onBind() throws Exception {
        context.startService(intent);
        firebaseBackgroundService.onBind(new Intent());
    }

    @Test
    public void onCreate() throws Exception {
        assertTrue(FirebaseBackgroundService.isServiceStarted());
    }
}