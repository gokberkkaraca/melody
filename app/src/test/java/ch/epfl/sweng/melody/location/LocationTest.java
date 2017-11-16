package ch.epfl.sweng.melody.location;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by maxwell on 15.11.17.
 */
public class LocationTest {
    private final double DELTA = 10e-5;
    private android.location.Location andLocation;

    @Before
    public void setUp() {
        andLocation = mock(android.location.Location.class);
        when(andLocation.getLatitude()).thenReturn(6.5);
        when(andLocation.getLongitude()).thenReturn(45.0);
        when(andLocation.getProvider()).thenReturn("Lausanne");
    }

    @Test
    public void distanceTo() throws Exception {
        ch.epfl.sweng.melody.location.Location location = new ch.epfl.sweng.melody.location.Location(andLocation);
        ch.epfl.sweng.melody.location.Location location2 = new ch.epfl.sweng.melody.location.Location(location);
        location2.setLongitude(6.7);
        location2.setLatitude(47.9);
        location2.setLocationName("TestName");
        assertEquals(location2.getLatitude(), 47.9, DELTA);
        assertEquals(location2.getLongitude(), 6.7, DELTA);
        assertEquals(location2.getLocationName(), "TestName");
        assertEquals(location.distanceTo(location), 0, 0.0005);
        assertEquals(location.distanceTo(location2), location2.distanceTo(location), DELTA);
    }

}