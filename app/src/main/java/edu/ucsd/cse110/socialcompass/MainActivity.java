package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        Intent add_locations_activity = new Intent(this, AddLocationsActivity.class);
        startActivity(add_locations_activity);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}