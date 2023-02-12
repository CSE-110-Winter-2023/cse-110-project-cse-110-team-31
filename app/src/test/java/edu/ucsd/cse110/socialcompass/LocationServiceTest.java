package edu.ucsd.cse110.socialcompass;

import android.util.Pair;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocationServiceTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

//    @Rule
//    public ActivityScenarioRule<Compass> rule = new ActivityScenarioRule<>(Compass.class);

    @Test
    public void testLocationService() {
        var scenario = ActivityScenario.launch(Compass.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        var mockDataSource = new MutableLiveData<Pair<Double, Double>>();

        scenario.onActivity(activity -> {
//            var locationService = LocationService.singleton(activity);
            activity.locationService.setMockOrientationSource(mockDataSource);

            mockDataSource.setValue(new Pair<Double, Double>(45d, 55d));

            TextView latLon = activity.findViewById(R.id.LatLon);

            System.out.println(latLon.getText());
            assert latLon.getText() == "45.0, 55.0";

//            System.out.println(activity.lat);
//            assert activity.lat == 45d && activity.lon == 55d;
//            assert 1==1;
        });
    }
}
