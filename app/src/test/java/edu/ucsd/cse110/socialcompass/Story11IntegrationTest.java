package edu.ucsd.cse110.socialcompass;

import android.os.Looper;
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
import org.robolectric.annotation.LooperMode;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;

@RunWith(RobolectricTestRunner.class)
public class Story11IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testPeopleAtPerimeterIfOutsideZoom(){
        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.friendLat = 5;
            activity.friendLon = 5;
            activity.houseLat = 4;
            activity.houseLon = 5.05;
            activity.lat = 5;
            activity.lon = 5.05;
            activity.renderDistances();

            var friendIcon = activity.findViewById(R.id.friend);
            var houseIcon = activity.findViewById(R.id.house);

            var friendLayoutParams = (ConstraintLayout.LayoutParams) friendIcon.getLayoutParams();
            var houseLayoutParams = (ConstraintLayout.LayoutParams) houseIcon.getLayoutParams();

            // should see that the distances have been updated (used hardcoded constrainttozoomratio)
            System.out.println(friendLayoutParams.circleRadius);
            System.out.println(houseLayoutParams.circleRadius);
            assert (friendLayoutParams.circleRadius < 500);
            assert (houseLayoutParams.circleRadius == 500);
        });
    }

    @Test
    public void testPeopleAtCorrectDistance(){
        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        // we essentially want to see that we're very near halfway from perimeter to center
        scenario.onActivity(activity -> {
            activity.friendLat = 5;
            activity.friendLon = 5.09157509;
            activity.lat = 5;
            activity.lon = 5;

            var friendIcon = activity.findViewById(R.id.friend);

            // call renderDistances

            activity.renderDistances();

            var friendLayoutParams = (ConstraintLayout.LayoutParams) friendIcon.getLayoutParams();

            // should see that the distances have been updated (used hardcoded constrainttozoomratio)
            assert (friendLayoutParams.circleRadius < 252);
            assert (friendLayoutParams.circleRadius > 248);
        });
    }
}
