package edu.ucsd.cse110.socialcompass;

import android.os.Looper;
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
import org.robolectric.annotation.LooperMode;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;

@RunWith(RobolectricTestRunner.class)
public class Story13IntegrationTest {

    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testZoomOutCompass(){
        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("stacy_scenario1");
            submit_button.performClick();
        });

        scenarioAddFriend.moveToState(Lifecycle.State.DESTROYED);

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.lon = -117.2376;
            activity.lat = 32.8811;
            activity.zoom_curr_ind = 0;
            activity.curr_zoom = 1;
            Button zoom_out = (Button) activity.findViewById(R.id.zoom_out_button);
            for(int i=0; i<activity.uids.length; i++) {
                if(activity.uids[i].equals("stacy_scenario1")) {
                    TextView locView= activity.friends.get(i);
                    Location currLoc = activity.locs.get(i);
                    currLoc.latitude = 32.871688;
                    currLoc.longitude = -117.213486;
                    activity.renderText(locView, currLoc.latitude, currLoc.longitude);
                    var layoutParams = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
                    assert layoutParams.circleRadius == 500;
                    assert locView.getVisibility() == View.INVISIBLE;
                    zoom_out.performClick();
                    assert layoutParams.circleRadius < 240 && layoutParams.circleRadius > 230;
                    assert locView.getVisibility() == View.VISIBLE;
                }
            }
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);

    }

    @Test
    public void testZoomInCompass(){

        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("brandon_scenario2");
            submit_button.performClick();
            uidEntry.setText("john_scenario2");
            submit_button.performClick();
        });

        scenarioAddFriend.moveToState(Lifecycle.State.DESTROYED);

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.lon = -117.2376;
            activity.lat = 32.8811;
            activity.zoom_curr_ind = 1;
            activity.curr_zoom = 10;
            activity.showCorrectCircles();
            activity.updateAllLocs();
            Button zoom_in = (Button) activity.findViewById(R.id.zoom_in_button);
            for(int i=0; i<activity.uids.length; i++) {
                if (activity.uids[i].equals("brandon_scenario2")) {
                    TextView locView = activity.friends.get(i);
                    Location currLoc = activity.locs.get(i);
                    currLoc.latitude = 32.971688;
                    currLoc.longitude = -117.213486;
                    activity.renderText(locView, currLoc.latitude, currLoc.longitude);
                    var layoutParams = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
                    System.out.println(layoutParams.circleRadius);
                    assert layoutParams.circleRadius < 500;
                    assert locView.getVisibility() == View.VISIBLE;
                } else if (activity.uids[i].equals("john_scenario2")) {
                    TextView locView2 = activity.friends.get(i);
                    Location currLoc2 = activity.locs.get(i);
                    currLoc2.latitude = 32.881688;
                    currLoc2.longitude = -117.233486;
                    activity.renderText(locView2, currLoc2.latitude, currLoc2.longitude);
                    var layoutParams2 = (ConstraintLayout.LayoutParams) locView2.getLayoutParams();
                    System.out.println(layoutParams2.circleRadius);
                    assert layoutParams2.circleRadius < (int)(2.75 * (165/2));
                    assert locView2.getVisibility() == View.VISIBLE;
                }
            }
            zoom_in.performClick();
            for(int i=0; i<activity.uids.length; i++) {
                if (activity.uids[i].equals("brandon_scenario2")) {
                    TextView locView3 = activity.friends.get(i);
                    var layoutParams3 = (ConstraintLayout.LayoutParams) locView3.getLayoutParams();
                    System.out.println(layoutParams3.circleRadius);
                    assert layoutParams3.circleRadius == 500;
                    assert locView3.getVisibility() == View.INVISIBLE;
                } else if (activity.uids[i].equals("john_scenario2")) {
                    TextView locView4 = activity.friends.get(i);
                    var layoutParams4 = (ConstraintLayout.LayoutParams) locView4.getLayoutParams();
                    System.out.println(layoutParams4.circleRadius);
                    assert layoutParams4.circleRadius > 54;
                    assert locView4.getVisibility() == View.VISIBLE;
                }
            }

        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void circleVisibilitiesAtZoomLevel(){
        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            ImageView circle_outer = (ImageView) activity.findViewById(R.id.circle_1);
            ImageView circle_middle = (ImageView) activity.findViewById(R.id.circle_2);
            ImageView circle_inner = (ImageView) activity.findViewById(R.id.circle_3);

            activity.zoom_curr_ind = 0;
            activity.showCorrectCircles();
            assert circle_outer.getVisibility() == View.VISIBLE &&
                    circle_middle.getVisibility() == View.INVISIBLE &&
                    circle_inner.getVisibility() == View.INVISIBLE;

            activity.zoom_curr_ind = 1;
            activity.showCorrectCircles();
            assert circle_outer.getVisibility() == View.VISIBLE &&
                    circle_middle.getVisibility() == View.VISIBLE &&
                    circle_inner.getVisibility() == View.INVISIBLE;

            activity.zoom_curr_ind = 2;
            activity.showCorrectCircles();
            assert circle_outer.getVisibility() == View.VISIBLE &&
                    circle_middle.getVisibility() == View.VISIBLE &&
                    circle_inner.getVisibility() == View.VISIBLE;

            activity.zoom_curr_ind = 1;
            activity.showCorrectCircles();
            assert circle_outer.getVisibility() == View.VISIBLE &&
                    circle_middle.getVisibility() == View.VISIBLE &&
                    circle_inner.getVisibility() == View.INVISIBLE;

            activity.zoom_curr_ind = 0;
            activity.showCorrectCircles();
            assert circle_outer.getVisibility() == View.VISIBLE &&
                    circle_middle.getVisibility() == View.INVISIBLE &&
                    circle_inner.getVisibility() == View.INVISIBLE;
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }



}
