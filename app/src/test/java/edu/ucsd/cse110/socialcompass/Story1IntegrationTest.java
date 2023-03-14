package edu.ucsd.cse110.socialcompass;

import android.util.Pair;

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
public class Story1IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testIconAngles() {
        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.friendLat = 5;
            activity.friendLon = 5;

            var mockDataSource = new MutableLiveData<Pair<Double, Double>>();

            activity.locationService.setMockOrientationSource(mockDataSource);
            activity.updateLocation();

            activity.orient = 0;
            mockDataSource.setValue(new Pair<>(0d, 5d));

            var friendIcon = activity.findViewById(R.id.friend);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) friendIcon.getLayoutParams();
            assert layoutParams.circleAngle == CompassActivity.angleFromCoordinate(0, 5, 5, 5);
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void testTwoIconAngles() {
        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.friendLat = 5;
            activity.friendLon = 5;
            activity.houseLat = 0;
            activity.houseLon = 0;

            var mockDataSource = new MutableLiveData<Pair<Double, Double>>();

            activity.locationService.setMockOrientationSource(mockDataSource);
            activity.updateLocation();

            activity.orient = 0;
            mockDataSource.setValue(new Pair<>(0d, 5d));

            var friendIcon = activity.findViewById(R.id.friend);
            var houseIcon = activity.findViewById(R.id.house);

            ConstraintLayout.LayoutParams layoutParamsF = (ConstraintLayout.LayoutParams) friendIcon.getLayoutParams();
            ConstraintLayout.LayoutParams layoutParamsH = (ConstraintLayout.LayoutParams) houseIcon.getLayoutParams();
            assert layoutParamsF.circleAngle == CompassActivity.angleFromCoordinate(0, 5, 5, 5)
                    && layoutParamsH.circleAngle == CompassActivity.angleFromCoordinate(0, 5, 0, 0);
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }
}
