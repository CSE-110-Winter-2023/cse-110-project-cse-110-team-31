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
        saveLocations();
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

        TextView my_long_view = findViewById(R.id.longitude1_view);
        TextView my_lat_view = findViewById(R.id.latitude1_view);
        TextView family_long_view = findViewById(R.id.longitude2_view);
        TextView family_lat_view = findViewById(R.id.latitude2_view);
        TextView friend_long_view = findViewById(R.id.longitude3_view);
        TextView friend_lat_view = findViewById(R.id.latitude3_view);

        my_long_view.setText(my_long);
        my_lat_view.setText(my_lat);
        family_long_view.setText(family_long);
        family_lat_view.setText(family_lat);
        friend_long_view.setText(friend_long);
        friend_lat_view.setText(friend_lat);
    }

    public void saveLocations() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextView my_long_view = findViewById(R.id.longitude1_view);
        TextView my_lat_view = findViewById(R.id.latitude1_view);
        TextView family_long_view = findViewById(R.id.longitude2_view);
        TextView family_lat_view = findViewById(R.id.latitude2_view);
        TextView friend_long_view = findViewById(R.id.longitude3_view);
        TextView friend_lat_view = findViewById(R.id.latitude3_view);

        editor.putString("my_long", my_long_view.getText().toString());
        editor.putString("my_lat", my_lat_view.getText().toString());

        // parent's home
        editor.putString("family_long", family_long_view.getText().toString());
        editor.putString("family_lat", family_lat_view.getText().toString());

        // friend's home
        editor.putString("friend_long", friend_long_view.getText().toString());
        editor.putString("friend_lat", friend_lat_view.getText().toString());

        editor.apply();
    }


    public void onSubmitClicked(View view) {
        finish();
    }
}