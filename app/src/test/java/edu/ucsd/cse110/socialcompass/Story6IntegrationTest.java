package edu.ucsd.cse110.socialcompass;

import static android.content.Context.MODE_PRIVATE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class Story6IntegrationTest {
    @Test
    public void enterNameTest() {

        var scenario = ActivityScenario.launch(EnterNameActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Name", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            TextView enterNameView = (TextView) activity.findViewById(R.id.enter_name_edittext);
            Button enterNameButton = (Button) activity.findViewById(R.id.enter_name_button);
            enterNameView.setText("SidTest");
            enterNameButton.performClick();

            String name = preferences.getString("enter_name", null);
            assertEquals("SidTest", name);
            editor.clear();
            editor.apply();
        });
    }
}
