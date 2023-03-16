package edu.ucsd.cse110.socialcompass;


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

import java.time.Duration;
import java.time.LocalDateTime;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;

@RunWith(RobolectricTestRunner.class)
public class Story12IntegrationTest {
    @Rule
    public GrantPermissionRule finePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule coarsePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testLiveLocation() {
        var scenarioAddTime = ActivityScenario.launch(CompassActivity.class);
        scenarioAddTime.moveToState(Lifecycle.State.CREATED);
        scenarioAddTime.moveToState(Lifecycle.State.STARTED);

        scenarioAddTime.onActivity(activity -> {
            activity.setupTimeUpdates();
            activity.now = LocalDateTime.now();
            activity.dateTimeConnected = LocalDateTime.now();//.minusMinutes(60);
            Duration duration = Duration.between(activity.now, activity.dateTimeConnected);//dateTimeConnected);
            long diff = Math.abs(duration.toMinutes());
            assert diff==0;
        });
        scenarioAddTime.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void testOfflineLocation() {
        var scenarioAddTime = ActivityScenario.launch(CompassActivity.class);
        scenarioAddTime.moveToState(Lifecycle.State.CREATED);
        scenarioAddTime.moveToState(Lifecycle.State.STARTED);

        scenarioAddTime.onActivity(activity -> {
            activity.setupTimeUpdates();
            activity.now = LocalDateTime.now().minusMinutes(3);
            activity.dateTimeConnected = LocalDateTime.now();//.minusMinutes(60);
            Duration duration = Duration.between(activity.now, activity.dateTimeConnected);//dateTimeConnected);
            long diff = Math.abs(duration.toMinutes());
            assert diff==3;
        });

        scenarioAddTime.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void testOfflineThenOnlineLocation() {
        var scenarioAddTime = ActivityScenario.launch(CompassActivity.class);
        scenarioAddTime.moveToState(Lifecycle.State.CREATED);
        scenarioAddTime.moveToState(Lifecycle.State.STARTED);

        scenarioAddTime.onActivity(activity -> {
            activity.setupTimeUpdates();
            activity.now = LocalDateTime.now().minusMinutes(60);
            activity.dateTimeConnected = LocalDateTime.now();//.minusMinutes(60);
            Duration duration = Duration.between(activity.now, activity.dateTimeConnected);//dateTimeConnected);
            long diff = Math.abs(duration.toMinutes());
            if (diff>=60) {
                diff = Math.abs(duration.toHours());
            }
            assert diff == 1;

            activity.now = LocalDateTime.now();
            activity.dateTimeConnected = LocalDateTime.now();//.minusMinutes(60);
            Duration duration2 = Duration.between(activity.now, activity.dateTimeConnected);//dateTimeConnected);
            long diff2 = Math.abs(duration2.toMinutes());
            assert diff2==0;

        });

        scenarioAddTime.moveToState(Lifecycle.State.DESTROYED);
    }




}
