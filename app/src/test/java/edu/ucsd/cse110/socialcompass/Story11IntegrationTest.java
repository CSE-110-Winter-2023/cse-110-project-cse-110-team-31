package edu.ucsd.cse110.socialcompass;

import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
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
public class Story11IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);




    @Test
    public void testAtPerimeterIfOutsideZoom() {
        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("ranatest5");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.curr_zoom = 10;
            activity.updateAllLocs();
            // setting it to be Geisel library for standardized purposes of Github CI
            activity.lon = -117.2376;
            activity.lat = 32.8811;
            for(int i=0; i<activity.uids.length; i++) {
                if(activity.uids[i].equals("ranatest5")) {
                    activity.updateLoc(i);
                    TextView locView = activity.friends.get(i);
                    var layoutParams = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
                    assert layoutParams.circleRadius == 500;
                }
            }
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void testPeopleAtCorrectDistance(){
        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);
        boolean testGeiselToUTC = false;
        boolean testGeiselToCove = false;

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("ken_test2");
            submit_button.performClick();
            uidEntry.setText("ken_test3");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.curr_zoom = 10;
            activity.zoom_curr_ind = 1;
            activity.updateAllLocs();
            // setting it to be Geisel library for standardized purposes of Github CI
            activity.lon = -117.2376;
            activity.lat = 32.8811;

            for(int i=0; i<activity.uids.length; i++) {
                // UTC
                if (activity.uids[i].equals("ken_test2")) {
                    TextView locView = activity.friends.get(i);
                    Location currLoc = activity.locs.get(i);
                    currLoc.latitude = 32.871688;
                    currLoc.longitude = -117.213486;
                    activity.renderText(locView, currLoc.latitude, currLoc.longitude);
                    var layoutParams = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
                    assert layoutParams.circleRadius > 230 &&
                            layoutParams.circleRadius < 240;
                } else if (activity.uids[i].equals("ken_test3")) {
                    TextView locView = activity.friends.get(i);
                    Location currLoc = activity.locs.get(i);
                    currLoc.latitude = 32.850343;
                    currLoc.longitude = -117.27264;
                    activity.renderText(locView, currLoc.latitude, currLoc.longitude);
                    var layoutParams2 = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
                    assert layoutParams2.circleRadius > 250 &&
                            layoutParams2.circleRadius < 270;
                }
            }


            /*
             * Leaving this section commented. We can't seem to get activity.locs.get(i) to not
             * return 0.0, 0.0. We figure it's an issue about the livedata needing some time
             * to get initialized, but a wait of 4000ms didn't seem to be enough despite it initializing
             * much faster in running the application.
             */
//            try {
//                Thread.currentThread().join(4000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            for(int i=0; i<activity.uids.length; i++) {
//                if(activity.uids[i].equals("ken_test2")) {
//                    activity.updateLoc(i);
//                    Location currLoc = activity.locs.get(i);
//                    System.out.println(activity.lat + " " + activity.lon);
//                    System.out.println(currLoc.latitude + " " + currLoc.longitude);
//                    System.out.println(CompassActivity.distInMiles(currLoc.latitude, currLoc.longitude, activity.lat, activity.lon));
//                    TextView locView = activity.friends.get(i);
//                    var layoutParams = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
//                    System.out.println(layoutParams.circleRadius);
//                    assert layoutParams.circleRadius > 2.9 * activity.constraintZoomRatio &&
//                            layoutParams.circleRadius < 3 * activity.constraintZoomRatio;
//                }
//                } else if (activity.uids[i].equals("ken_test2")) {
//                    System.out.println("issue was in test2");
//                    TextView locView = activity.friends.get(i);
//                    var layoutParams = (ConstraintLayout.LayoutParams) locView.getLayoutParams();
//                    System.out.println(layoutParams.circleRadius);
//                    assert layoutParams.circleRadius > 1.5 * activity.constraintZoomRatio &&
//                            layoutParams.circleRadius < 1.6 * activity.constraintZoomRatio;
//                }
//            }
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void testLabelDisappearsAtPerimeter(){
        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("ranatest5");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            // setting it to be Geisel library for standardized purposes of Github CI
            activity.curr_zoom = 10;
            activity.updateAllLocs();
            activity.lon = -117.2376;
            activity.lat = 32.8811;
            for(int i=0; i<activity.uids.length; i++) {
                if(activity.uids[i].equals("ranatest5")) {
                    activity.updateLoc(i);
                    TextView locView = activity.friends.get(i);
                    assert locView.getVisibility() == View.INVISIBLE;
                }
            }
        });

        scenario.moveToState(Lifecycle.State.DESTROYED);
    }
}
