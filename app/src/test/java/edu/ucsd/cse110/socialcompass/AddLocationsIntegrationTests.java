package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AddLocationsIntegrationTests {

    @Test
    public void testSubmittedSingularValuePersists() {
        var scenario = ActivityScenario.launch(AddLocationsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            TextView my_long_view = (TextView) activity.findViewById(R.id.longitude1_view);
            Button submit_button = (Button) activity.findViewById(R.id.submit_button);
            my_long_view.setText("1");
            submit_button.performClick();
        });

        var scenario2 = ActivityScenario.launch(AddLocationsActivity.class);
        scenario2.moveToState(Lifecycle.State.CREATED);
        scenario2.moveToState(Lifecycle.State.STARTED);

        scenario2.onActivity(activity2 -> {
            TextView my_long_view = (TextView) activity2.findViewById(R.id.longitude1_view);
            assertEquals("1", my_long_view.getText().toString());
        });


    }

    @Test
    public void testSubmittingMultipleValuesPersists(){
        var scenario = ActivityScenario.launch(AddLocationsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            TextView my_long_view = (TextView) activity.findViewById(R.id.longitude1_view);
            TextView my_long2_view = (TextView) activity.findViewById(R.id.longitude2_view);
            TextView my_lat3_view = (TextView) activity.findViewById(R.id.latitude3_view);
            Button submit_button = (Button) activity.findViewById(R.id.submit_button);

            my_long_view.setText("1");
            my_long2_view.setText("2");
            my_lat3_view.setText("3");
            submit_button.performClick();

            var scenario2 = ActivityScenario.launch(AddLocationsActivity.class);
            scenario2.moveToState(Lifecycle.State.CREATED);
            scenario2.moveToState(Lifecycle.State.STARTED);

            scenario2.onActivity(activity2 -> {
                TextView my_long_view_2 = (TextView) activity2.findViewById(R.id.longitude1_view);
                TextView my_long2_view_2 = (TextView) activity2.findViewById(R.id.longitude2_view);
                TextView my_lat3_view_2 = (TextView) activity2.findViewById(R.id.latitude3_view);
                assertEquals("1", my_long_view_2.getText().toString());
                assertEquals("2", my_long2_view_2.getText().toString());
                assertEquals("3", my_lat3_view_2.getText().toString());
            });

        });
    }

    @Test
    public void testSubmittingNoFullCoords() {
        var scenario = ActivityScenario.launch(AddLocationsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            TextView my_long_view = (TextView) activity.findViewById(R.id.longitude1_view);
            Button submit_button = (Button) activity.findViewById(R.id.submit_button);
            my_long_view.setText("1");
            submit_button.performClick();
        });

        assertEquals(scenario.getState(), Lifecycle.State.STARTED);
    }

    @Test
    public void testSubmittingFullCoord() {
//        var scenario = ActivityScenario.launch(AddLocationsActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//
//        scenario.onActivity(activity -> {
//            TextView my_long_view = (TextView) activity.findViewById(R.id.longitude1_view);
//            TextView my_lat_view = (TextView) activity.findViewById(R.id.latitude1_view);
//            Button submit_button = (Button) activity.findViewById(R.id.submit_button);
//            my_long_view.setText("1");
//            my_lat_view.setText("2");
//            submit_button.performClick();
//        });
//
//        assertEquals(scenario.getState(), Lifecycle.State.DESTROYED);

    }

    // test not submitting and exiting (values shouldn't be saved)
    @Test
    public void testNotSubmittingNotPersists(){
        var scenario = ActivityScenario.launch(AddLocationsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            TextView my_long_view = (TextView) activity.findViewById(R.id.longitude1_view);
            Button submit_button = (Button) activity.findViewById(R.id.submit_button);
            my_long_view.setText("1");
            scenario.moveToState(Lifecycle.State.DESTROYED);
        });

        var scenario2 = ActivityScenario.launch(AddLocationsActivity.class);
        scenario2.moveToState(Lifecycle.State.CREATED);
        scenario2.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            TextView my_long_view = (TextView) activity.findViewById(R.id.longitude1_view);
            assertEquals("", my_long_view.getText().toString());
        });


    }
}
