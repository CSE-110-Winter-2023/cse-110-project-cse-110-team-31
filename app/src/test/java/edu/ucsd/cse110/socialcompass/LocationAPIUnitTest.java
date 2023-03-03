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

        assert loc.label.equals("Rana");
    }

    @Test
    public void putLocationRunsTest() {
        Location loc = new Location("ranatest2", 30, 40);
        loc.private_code = "notouch";
        loc.label = "Rana";

        System.out.println(loc.toLimitedJSON(new String[]{"private_code", "label", "latitude", "longitude"}));

        LocationAPI api = LocationAPI.provide();

        api.putLocation(loc);
    }

    @Test
    public void patchLocationRunsTest() {
        Location loc = new Location("ranatest2");
        loc.private_code = "notouch";
        loc.isPublic = false;

        System.out.println(loc.toLimitedJSON(new String[]{"private_code", "isPublic"}));

        LocationAPI api = LocationAPI.provide();

        api.patchLocation(loc);
    }

    @Test
    public void getPutLocationTest() throws ExecutionException, InterruptedException, TimeoutException {
        LocationAPI api = LocationAPI.provide();

        Location loc = new Location("ranatest3", 35, 45);
        loc.private_code = "notouch";
        loc.label = "Rana";

        api.putLocation(loc);

        Thread.currentThread().join(200);

        Location receivedLoc = api.getLocation("ranatest3");

        assert receivedLoc.longitude == loc.longitude
                && receivedLoc.latitude == loc.latitude
                && receivedLoc.label.equals(loc.label);

        api.deleteLoc(loc);
    }
}
