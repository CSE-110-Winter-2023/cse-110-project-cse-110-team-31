package edu.ucsd.cse110.socialcompass;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;

@RunWith(RobolectricTestRunner.class)
public class LocationAPIUnitTest {
    @Test
    public void getLocationTest() throws ExecutionException, InterruptedException, TimeoutException {
        LocationAPI api = LocationAPI.provide();

        Location loc = api.getLocation("point-nemo");

        System.out.println(loc.latitude);
    }

    @Test void putLocationTest() {

    }
}
