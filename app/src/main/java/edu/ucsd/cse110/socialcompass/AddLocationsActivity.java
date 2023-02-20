package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);
        String my_long = String.valueOf(preferences.getFloat("my_long", 0f));
        String my_lat = String.valueOf(preferences.getFloat("my_lat", 0f));
        String family_long = String.valueOf(preferences.getFloat("family_long", 0f));
        String family_lat = String.valueOf(preferences.getFloat("family_lat", 0f));
        String friend_long = String.valueOf(preferences.getFloat("friend_long", 0f));
        String friend_lat = String.valueOf(preferences.getFloat("friend_lat", 0f));
        String home_label = preferences.getString("home_label", "");
        String friend_label = preferences.getString("friend_label", "");
        String family_label = preferences.getString("family_label", "");

        TextView my_long_view = findViewById(R.id.home_long_view);
        TextView my_lat_view = findViewById(R.id.home_lat_view);
        TextView family_long_view = findViewById(R.id.family_long_view);
        TextView family_lat_view = findViewById(R.id.family_lat_view);
        TextView friend_long_view = findViewById(R.id.friend_long_view);
        TextView friend_lat_view = findViewById(R.id.friend_lat_view);
        TextView home_label_view = findViewById(R.id.home_label_view);
        TextView friend_label_view = findViewById(R.id.friend_label_view);
        TextView family_label_view = findViewById(R.id.family_label_view);

        if(preferences.contains("my_long")) my_long_view.setText(my_long);
        if(preferences.contains("my_lat")) my_lat_view.setText(my_lat);
        if(preferences.contains("family_long")) family_long_view.setText(family_long);
        if(preferences.contains("family_lat")) family_lat_view.setText(family_lat);
        if(preferences.contains("friend_long")) friend_long_view.setText(friend_long);
        if(preferences.contains("friend_lat")) friend_lat_view.setText(friend_lat);
        home_label_view.setText(home_label);
        friend_label_view.setText(friend_label);
        family_label_view.setText(family_label);
    }

    public void saveLocations() {
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();

        TextView my_long_view = findViewById(R.id.home_long_view);
        TextView my_lat_view = findViewById(R.id.home_lat_view);
        TextView family_long_view = findViewById(R.id.family_long_view);
        TextView family_lat_view = findViewById(R.id.family_lat_view);
        TextView friend_long_view = findViewById(R.id.friend_long_view);
        TextView friend_lat_view = findViewById(R.id.friend_lat_view);
        TextView home_label_view = findViewById(R.id.home_label_view);
        TextView friend_label_view = findViewById(R.id.friend_label_view);
        TextView family_label_view = findViewById(R.id.family_label_view);

        if(!my_long_view.getText().toString().equals("")) editor.putFloat("my_long", Float.parseFloat(my_long_view.getText().toString()));
        if(!my_lat_view.getText().toString().equals("")) editor.putFloat("my_lat", Float.parseFloat(my_lat_view.getText().toString()));

        if(!family_long_view.getText().toString().equals("")) editor.putFloat("family_long", Float.parseFloat(family_long_view.getText().toString()));
        if(!family_lat_view.getText().toString().equals("")) editor.putFloat("family_lat", Float.parseFloat(family_lat_view.getText().toString()));

        if(!friend_long_view.getText().toString().equals("")) editor.putFloat("friend_long", Float.parseFloat(friend_long_view.getText().toString()));
        if(!friend_lat_view.getText().toString().equals("")) editor.putFloat("friend_lat", Float.parseFloat(friend_lat_view.getText().toString()));

        editor.putString("home_label", home_label_view.getText().toString());
        editor.putString("family_label", family_label_view.getText().toString());
        editor.putString("friend_label", friend_label_view.getText().toString());

        editor.apply();
    }

    public boolean locationPairEntered(String key1, String key2) {
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);
        return (preferences.contains(key1) &&
                preferences.contains(key2));
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
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(AddLocationsActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Must enter at least 1 location");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
    }
}