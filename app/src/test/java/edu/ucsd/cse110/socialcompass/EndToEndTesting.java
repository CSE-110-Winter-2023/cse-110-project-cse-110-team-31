/**
 * Testing for the entire milestone
 * This was originally testing for story 4, but since that was our last story
 * it ended up testing the entire MS
 */

package edu.ucsd.cse110.socialcompass;

import android.util.Pair;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
public class EndToEndTesting {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testIntegration() {
        var mockOrientation = new MutableLiveData<Float>();
        var mockLocation = new MutableLiveData<Pair<Double, Double>>();

        var scenarioAddLocations = ActivityScenario.launch(AddLocationsActivity.class);
        scenarioAddLocations.moveToState(Lifecycle.State.CREATED);
        scenarioAddLocations.moveToState(Lifecycle.State.STARTED);

        scenarioAddLocations.onActivity(activity -> {
            TextView friend_long_view = (TextView) activity.findViewById(R.id.friend_long_view);
            TextView friend_lat_view = (TextView) activity.findViewById(R.id.friend_lat_view);
            TextView house_long_view = (TextView) activity.findViewById(R.id.home_long_view);
            TextView house_lat_view = (TextView) activity.findViewById(R.id.home_lat_view);
            Button submit_button = (Button) activity.findViewById(R.id.submit_button);
            friend_long_view.setText("10");
            friend_lat_view.setText("5");
            house_long_view.setText("5");
            house_lat_view.setText("10");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.locationService.setMockOrientationSource(mockLocation);
            activity.updateLocation();
            activity.orientationService.setMockOrientationData(mockOrientation);
            activity.updateOrientation();

            mockOrientation.setValue(0f);
            mockLocation.setValue(new Pair<>(5d, 5d));

            ImageView friend = activity.findViewById(R.id.friend);
            ConstraintLayout.LayoutParams friendLayoutParams = (ConstraintLayout.LayoutParams) friend.getLayoutParams();
            ImageView house = activity.findViewById(R.id.house);
            ConstraintLayout.LayoutParams houseLayoutParams = (ConstraintLayout.LayoutParams) house.getLayoutParams();

            assert houseLayoutParams.circleAngle == 0 && friendLayoutParams.circleAngle == 90;

            mockOrientation.setValue((float)Math.PI/2); // rotate 90 degrees

            assert Math.round(houseLayoutParams.circleAngle) == -90 && Math.round(friendLayoutParams.circleAngle) == 0;
        });
    }
}
