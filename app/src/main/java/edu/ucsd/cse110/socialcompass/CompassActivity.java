package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

public class CompassActivity extends AppCompatActivity {
    public LocationService locationService;

    double lat, lon;

    double friendLat = 32.9857, friendLon = -117.266;
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

        updateLocation();
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
        double latDistance = lat - otherLat;
        double lonDistance = lon - otherLon;
        double degrees = Math.atan(lonDistance / latDistance)*180/Math.PI;
        System.out.println(degrees);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        layoutParams.circleAngle = (float)degrees;
        image.setLayoutParams(layoutParams);
    }
}
