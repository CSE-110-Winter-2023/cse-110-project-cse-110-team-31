package edu.ucsd.cse110.socialcompass;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.room.SkipQueryVerification;
import androidx.test.core.app.ActivityScenario;
import androidx.test.filters.FlakyTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class Story9IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void test1UID() {
        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("ranatest2");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            boolean uidExists=false;
            for(int i=0; i < activity.uids.length; i++) {
                if(activity.uids[i].equals("ranatest2")) {
                    uidExists=true;
                    break;
                }
            }
            assert uidExists;
        });
    }

    @Test
    public void test2UID() {
        var scenarioAddFriend = ActivityScenario.launch(AddFriendActivity.class);
        scenarioAddFriend.moveToState(Lifecycle.State.CREATED);
        scenarioAddFriend.moveToState(Lifecycle.State.STARTED);

        scenarioAddFriend.onActivity(activity -> {
            TextView uidEntry = (TextView) activity.findViewById(R.id.enterFriendID);
            Button submit_button = (Button) activity.findViewById(R.id.addFriendSubmitButton);
            uidEntry.setText("ranatest2");
            submit_button.performClick();
            uidEntry.setText("ranatest5");
            submit_button.performClick();
        });

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            boolean uid1Exists=false;
            boolean uid2Exists=false;
            for(int i=0; i<activity.uids.length; i++) {
                if (activity.uids[i].equals("ranatest2")) uid1Exists = true;
                else if (activity.uids[i].equals("ranatest5")) uid2Exists=true;
                if (uid1Exists && uid2Exists) break;
            }
            assert uid1Exists && uid2Exists;
        });
    }
}
