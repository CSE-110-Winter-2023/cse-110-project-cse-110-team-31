package edu.ucsd.cse110.socialcompass;

import org.junit.Test;

public class DistanceUnitTest {
    @Test
    public void testLatDist() {
        // test base case
        assert (CompassActivity.latDistInMiles(1, 0) == 69);
        // test dist is scalar.
        assert (CompassActivity.latDistInMiles(0, 1) == 69);
    }

    @Test
    public void testLonDist() {
        // test base case
        assert (CompassActivity.lonDistInMiles(1, 0) == 54.6);
        // test dist is scalar.
        assert (CompassActivity.lonDistInMiles(0, 1) == 54.6);
    }

    @Test
    public void testDistInMiles() {
        // test that the distance between itself is 0
        assert (CompassActivity.distInMiles(1, 1, 1, 1) == 0);
        // test that the distance calcs are relatively accurate
        assert (CompassActivity.distInMiles(2, 2, 1, 1) > 87 && CompassActivity.distInMiles(2, 2, 1, 1) < 88);
        // test that distance is a scalar.
        assert (CompassActivity.distInMiles(2,2,1,1) == CompassActivity.distInMiles(1,1,2,2));
    }
}