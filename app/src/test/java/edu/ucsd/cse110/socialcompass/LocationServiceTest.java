package edu.ucsd.cse110.socialcompass;

import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.lifecycle.MutableLiveData;
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

    @Test
    public void testLocationService() {
        var mockDataSource = new MutableLiveData<Pair<Double, Double>>();

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.locationService.setMockOrientationSource(mockDataSource);
            activity.updateLocation();

            mockDataSource.setValue(new Pair<>(45d, 55d));

            assert activity.lat == 45.0d && activity.lon == 55.0d;
        });
    }
}
