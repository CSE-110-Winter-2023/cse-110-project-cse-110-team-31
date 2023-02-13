package edu.ucsd.cse110.socialcompass;


import org.junit.Test;

public class AngleUnitTest {
    @Test
    public void testSimpleAngle() {
        assert CompassActivity.angleFromCoordinate(0, 0, 1, 1) == 45;
    }
    @Test
    public void testVerticalAngle() {
        assert CompassActivity.angleFromCoordinate(0, 0, 0, 1) == 90;
    }
    @Test
    public void testHorizontalAngle() {
        assert CompassActivity.angleFromCoordinate(0, 0, 1, 0) == 0;
    }
}
