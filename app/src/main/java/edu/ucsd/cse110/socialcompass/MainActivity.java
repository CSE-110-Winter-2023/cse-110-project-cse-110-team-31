package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent add_locations_activity = new Intent(this, EnterNameActivity.class); // TODO change back to AddFriendActivity
        startActivity(add_locations_activity);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onLaunchCompass(View view) {
        Intent intent = new Intent(this, CompassActivity.class); // TODO change back to CompassActivity
        startActivity(intent);
    }
}