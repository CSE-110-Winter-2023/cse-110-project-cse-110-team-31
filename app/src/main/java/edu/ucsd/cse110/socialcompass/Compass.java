package edu.ucsd.cse110.socialcompass;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public class Compass extends AppCompatActivity implements LocationListener {
    double lat, lon;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        getLocation();
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    setupListener();
                } else {
                    TextView latLon = findViewById(R.id.LatLon);
                    latLon.setText("Please enable permissions in settings");
                }
            });

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setupListener();
    }

    private void setupListener() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            TextView latLon = findViewById(R.id.LatLon);
            latLon.setText("no permission");
            requestPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION);
            requestPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, (LocationListener) this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        TextView latLon = findViewById(R.id.LatLon);
        lat=location.getLatitude();
        lon=location.getLongitude();
        latLon.setText(lat+" "+lon);
    }

    @Override
    public void onProviderDisabled(String provider) {
        TextView latLon = findViewById(R.id.LatLon);
        latLon.setText("heck");
    }
}