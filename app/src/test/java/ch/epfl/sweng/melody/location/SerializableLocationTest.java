package ch.epfl.sweng.melody.location;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SerializableLocationTest {
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
        SerializableLocation serializableLocation = new SerializableLocation(andLocation);
        SerializableLocation serializableLocation2 = new SerializableLocation(serializableLocation);
        serializableLocation2.setLongitude(6.7);
        serializableLocation2.setLatitude(47.9);
        serializableLocation2.setLocationName("TestName");
        assertEquals(serializableLocation2.getLatitude(), 47.9, DELTA);
        assertEquals(serializableLocation2.getLongitude(), 6.7, DELTA);
        assertEquals(serializableLocation2.getLocationName(), "TestName");
        assertEquals(serializableLocation.distanceTo(serializableLocation), 0, 0.0005);
        assertEquals(serializableLocation.distanceTo(serializableLocation2), serializableLocation2.distanceTo(serializableLocation), DELTA);
    }

}