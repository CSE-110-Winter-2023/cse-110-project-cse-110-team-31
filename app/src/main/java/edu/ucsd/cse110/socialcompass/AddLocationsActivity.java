package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddLocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_locations);
        loadLocations();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void loadLocations() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String my_long = preferences.getString("my_long", "");
        String my_lat = preferences.getString("my_lat", "");
        String family_long = preferences.getString("family_long", "");
        String family_lat = preferences.getString("family_lat", "");
        String friend_long = preferences.getString("friend_long", "");
        String friend_lat = preferences.getString("friend_lat", "");
        String home_label = preferences.getString("home_label", "");
        String friend_label = preferences.getString("friend_label", "");
        String family_label = preferences.getString("family_label", "");

        TextView my_long_view = findViewById(R.id.family_long_view);
        TextView my_lat_view = findViewById(R.id.friend_lat_view);
        TextView family_long_view = findViewById(R.id.longitude2_view);
        TextView family_lat_view = findViewById(R.id.home_lat_view);
        TextView friend_long_view = findViewById(R.id.home_long_view);
        TextView friend_lat_view = findViewById(R.id.family_lat_view);
        TextView home_label_view = findViewById(R.id.home_label_view);
        TextView friend_label_view = findViewById(R.id.friend_label_view);
        TextView family_label_view = findViewById(R.id.family_label_view);

        my_long_view.setText(my_long);
        my_lat_view.setText(my_lat);
        family_long_view.setText(family_long);
        family_lat_view.setText(family_lat);
        friend_long_view.setText(friend_long);
        friend_lat_view.setText(friend_lat);
        home_label_view.setText(home_label);
        friend_label_view.setText(friend_label);
        family_label_view.setText(family_label);
    }

    public void saveLocations() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextView my_long_view = findViewById(R.id.family_long_view);
        TextView my_lat_view = findViewById(R.id.friend_lat_view);
        TextView family_long_view = findViewById(R.id.longitude2_view);
        TextView family_lat_view = findViewById(R.id.home_lat_view);
        TextView friend_long_view = findViewById(R.id.home_long_view);
        TextView friend_lat_view = findViewById(R.id.family_lat_view);
        TextView home_label_view = findViewById(R.id.home_label_view);
        TextView friend_label_view = findViewById(R.id.friend_label_view);
        TextView family_label_view = findViewById(R.id.family_label_view);

        editor.putString("my_long", my_long_view.getText().toString());
        editor.putString("my_lat", my_lat_view.getText().toString());

        editor.putString("family_long", family_long_view.getText().toString());
        editor.putString("family_lat", family_lat_view.getText().toString());

        editor.putString("friend_long", friend_long_view.getText().toString());
        editor.putString("friend_lat", friend_lat_view.getText().toString());

        editor.putString("home_label", home_label_view.getText().toString());
        editor.putString("family_label", family_label_view.getText().toString());
        editor.putString("friend_label", friend_label_view.getText().toString());

        editor.apply();
    }

    public boolean locationPairEntered(String key1, String key2) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return (!preferences.getString(key1, "").equals("") &&
                !preferences.getString(key2, "").equals(""));
    }

    public boolean atLeastOneLocationExists(){
        return locationPairEntered("my_long", "my_lat") ||
                locationPairEntered("family_long", "family_lat") ||
                locationPairEntered("friend_long", "friend_lat");
    }

    public void onSubmitClicked(View view) {
        saveLocations();
        if (atLeastOneLocationExists()) {
            finish();
        }
    }
}