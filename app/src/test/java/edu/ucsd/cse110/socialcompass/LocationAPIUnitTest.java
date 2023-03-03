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

        Location loc = api.getLocation("ranatest");

        System.out.println(loc.toJSON());
    }

    @Test
    public void putLocationTest() throws ExecutionException, InterruptedException, TimeoutException {
        Location loc = new Location("ranatest2", 30, 40);
        loc.private_code = "notouch";
        loc.label = "Rana";

        System.out.println(loc.toPutJSON());

        LocationAPI api = LocationAPI.provide();

        api.putLocation(loc);
    }
}
