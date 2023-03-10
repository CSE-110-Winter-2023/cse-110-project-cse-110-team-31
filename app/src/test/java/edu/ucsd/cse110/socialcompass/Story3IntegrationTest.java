package edu.ucsd.cse110.socialcompass;

import android.util.Pair;
import android.view.View;
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
public class Story3IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testIntegration() {
        var scenarioAddLocations = ActivityScenario.launch(AddLocationsActivity.class);
        scenarioAddLocations.moveToState(Lifecycle.State.CREATED);
        scenarioAddLocations.moveToState(Lifecycle.State.STARTED);

        scenarioAddLocations.onActivity(activity -> {
            TextView friend_long_view = (TextView) activity.findViewById(R.id.friend_long_view);
            TextView friend_lat_view = (TextView) activity.findViewById(R.id.friend_lat_view);
            TextView house_long_view = (TextView) activity.findViewById(R.id.home_long_view);
            TextView house_lat_view = (TextView) activity.findViewById(R.id.home_lat_view);
            TextView friend_label_view = (TextView) activity.findViewById(R.id.friend_label_view);
            TextView house_label_view = (TextView) activity.findViewById(R.id.home_label_view);
            Button submit_button = (Button) activity.findViewById(R.id.submit_button);
            friend_long_view.setText("10");
            friend_lat_view.setText("5");
            house_long_view.setText("5");
            house_lat_view.setText("10");
            friend_label_view.setText("friend_test");
            house_label_view.setText("house_test");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            // THESE TESTS ARE NO LONGER APPLICABLE AS WE NO LONGER RENDER
            // THE OLD ICONS
//            TextView friend_label = activity.findViewById(R.id.friend_label_view);
//            assert friend_label.getVisibility() == View.VISIBLE && friend_label.getText().toString().equals("friend_test");
//            TextView house_label = activity.findViewById(R.id.house_label_view);
//            assert house_label.getVisibility() == View.VISIBLE && house_label.getText().toString().equals("house_test");
//            TextView family_label = activity.findViewById(R.id.family_label_view);
//            assert family_label.getVisibility() == View.INVISIBLE;
        });
    }
}
