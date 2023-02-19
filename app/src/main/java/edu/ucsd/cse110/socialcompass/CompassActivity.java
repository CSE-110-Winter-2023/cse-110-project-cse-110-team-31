package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

public class CompassActivity extends AppCompatActivity {
    public LocationService locationService;
    public OrientationService orientationService;

    double lat, lon;
    double orient = 0;

    double friendLat, friendLon;
    double houseLat, houseLon;
    double familyLat, familyLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);

        getLocations();
        updateLocation();
        updateOrientation();
        System.out.println(friendLat);
    }

    void updateOrientation() {
        orientationService.getOrientation().observe(this, orientation -> {
            orient = orientation;
            setImageDirections();
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        getLocations();
        setImageDirections();
    }

    public void getLocations(){
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);

        try {
            if(preferences.getAll().containsKey("my_long"))  houseLon = preferences.getFloat("my_long", (float)0);
            if(preferences.getAll().containsKey("my_lat"))  houseLat = preferences.getFloat("my_lat", (float)0);

            if(preferences.getAll().containsKey("friend_long"))  friendLon = preferences.getFloat("friend_long", (float)0);
            if(preferences.getAll().containsKey("friend_long"))  friendLat = preferences.getFloat("friend_lat", (float)0);

            if(preferences.getAll().containsKey("family_long"))  familyLon = preferences.getFloat("family_long", (float)0);
            if(preferences.getAll().containsKey("family_lat"))  familyLat = preferences.getFloat("family_lat", (float)0);
        } catch(Exception e) {
            System.out.println("Parse error");
        }

        System.out.println(friendLat+" "+friendLon);

        String home_label = preferences.getString("home_label", "");
        String friend_label = preferences.getString("friend_label", "");
        String family_label = preferences.getString("family_label", "");
    }

    void updateLocation() {
        locationService.getLocation().observe(this, loc -> {
            lat = loc.first;
            lon = loc.second;
            setImageDirections();
        });
    }

    void setImageDirections() {
        ImageView friend = findViewById(R.id.friend);
        renderImage(friend, friendLat, friendLon);
        ImageView family = findViewById(R.id.family);
        renderImage(family, familyLat, familyLon);
        ImageView house = findViewById(R.id.house);
        renderImage(house, houseLat, houseLon);
    }

    void renderImage(ImageView image, double otherLat, double otherLon) {
        double degrees = angleFromCoordinate(lat, lon, otherLat, otherLon);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        layoutParams.circleAngle = (float)(degrees+orient*(180 / Math.PI));
        image.setLayoutParams(layoutParams);
    }

    public static double angleFromCoordinate(double lat1, double long1, double lat2,
                                       double long2) {
        double brng = Math.atan2(lat1 - lat2, long1 - long2);
        brng = brng * (180 / Math.PI);
        brng = 360 - brng;
        return (brng - 90 + 360) % 360;
    }
}
