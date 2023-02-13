package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

public class CompassActivity extends AppCompatActivity {
    public LocationService locationService;

    double lat, lon;

    double friendLat = 32.910044, friendLon = -117.146084;
    double houseLat = 32.860239, houseLon = -117.229796;
    double familyLat = 32.9881, familyLon = -117.2411;

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
        double degrees = angleFromCoordinate(lat, lon, otherLat, otherLon);
        System.out.println(degrees);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        layoutParams.circleAngle = (float)degrees;
        image.setLayoutParams(layoutParams);
    }

    private double angleFromCoordinate(double lat1, double long1, double lat2,
                                       double long2) {
        double brng = Math.atan2(lat1 - lat2, long1 - long2);
        brng = brng * (180 / Math.PI);
        brng = 360 - brng;
        return brng - 90;
    }
}
