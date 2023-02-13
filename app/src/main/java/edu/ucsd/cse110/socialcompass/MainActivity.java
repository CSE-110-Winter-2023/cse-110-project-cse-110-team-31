package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (!checkExistsLocation()){
//            Intent add_locations_activity = new Intent(this, AddLocationsActivity.class);
//            startActivity(add_locations_activity);
//        }
        Intent add_locations_activity = new Intent(this, AddLocationsActivity.class);
        startActivity(add_locations_activity);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//
//    public boolean locationPairEntered(String key1, String key2) {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        return (!preferences.getString(key1, "").equals("") &&
//                !preferences.getString(key2, "").equals(""));
//    }
//
//    public boolean checkExistsLocation() {
//        return locationPairEntered("my_long", "my_lat") ||
//                locationPairEntered("family_long", "family_lat") ||
//                locationPairEntered("friend_long", "friend_lat");
//    }
}