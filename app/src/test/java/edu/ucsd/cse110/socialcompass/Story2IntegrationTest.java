package edu.ucsd.cse110.socialcompass;

import android.util.Pair;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class Story2IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testIntegration() {
        var mockOrientation = new MutableLiveData<Float>();
        var mockLocation = new MutableLiveData<Pair<Double, Double>>();

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.locationService.setMockOrientationSource(mockLocation);
            activity.updateLocation();
            activity.orientationService.setMockOrientationData(mockOrientation);
            activity.updateOrientation();

            activity.friendLat = 5;
            activity.friendLon = 10; // straight right
            activity.houseLat = 10;
            activity.houseLon = 5; // straight up

            mockOrientation.setValue(0f);
            mockLocation.setValue(new Pair<>(5d, 5d));

            ImageView friend = activity.findViewById(R.id.friend);
            ConstraintLayout.LayoutParams friendLayoutParams = (ConstraintLayout.LayoutParams) friend.getLayoutParams();
            ImageView house = activity.findViewById(R.id.house);
            ConstraintLayout.LayoutParams houseLayoutParams = (ConstraintLayout.LayoutParams) house.getLayoutParams();

            assert houseLayoutParams.circleAngle == 0 && friendLayoutParams.circleAngle == 90;

            mockOrientation.setValue((float)Math.PI/2); // rotate 90 degrees

            assert houseLayoutParams.circleAngle == 90 && friendLayoutParams.circleAngle == 180;
        });
    }
}
