package edu.ucsd.cse110.socialcompass;

import static android.view.View.INVISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;
import edu.ucsd.cse110.socialcompass.model.LocationViewModel;

public class CompassActivity extends AppCompatActivity {

    int spanCount = 1000;
    public RecyclerView recyclerView;
    public LocationService locationService;
    public OrientationService orientationService;

    Location loc;
    LocationAPI api;

    // TODO: these two need to be updated by sharedpreferences or something
    String UID = "ranatest4";
    String label = "hi I am Rana";
    String priv_key = "notouch";

    double lat, lon;
    double orient = 0;

    boolean houseDisplay, friendDisplay, familyDisplay;
    double friendLat, friendLon;
    double houseLat, houseLon;
    double familyLat, familyLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        CompassAdapter compassAdapter = new CompassAdapter();
        compassAdapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.friends);
        recyclerView.setLayoutManager(new AutoFitGridLayoutManager(this, spanCount));
        recyclerView.setAdapter(compassAdapter);


//        List<Friend> friends = Friend.loadJSON(this,"demo_friends.json");
//        Log.d("CompassActivity", friends.toString());
        compassAdapter.setFriendsList(Friend.loadJSON(this,"demo_friends.json"));




        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);

        loc = new Location(UID);
        api = LocationAPI.provide();
        loc.label = label;
        loc.private_code = priv_key;

        getLocations();
        updateLocation();
        if(!mockOrientationWithBox()) updateOrientation();

        setFriendLocation();
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

    boolean mockOrientationWithBox() {
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);
        if(preferences.contains("orientation")) {
            orient = (Math.PI/180)*preferences.getFloat("orientation", 0f);
            return true;
        }
        return false;
    }

    public void getLocations(){
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);

        houseDisplay=true;
        friendDisplay=true;
        familyDisplay=true;

        try {
            if((!preferences.contains("my_long")||(!preferences.contains("my_lat")))) houseDisplay = false;
            if(preferences.getAll().containsKey("my_long"))  houseLon = preferences.getFloat("my_long", (float)0);
            if(preferences.getAll().containsKey("my_lat"))  houseLat = preferences.getFloat("my_lat", (float)0);

            if((!preferences.contains("friend_long")||(!preferences.contains("friend_lat")))) friendDisplay = false;
            if(preferences.getAll().containsKey("friend_long"))  friendLon = preferences.getFloat("friend_long", (float)0);
            if(preferences.getAll().containsKey("friend_long"))  friendLat = preferences.getFloat("friend_lat", (float)0);

            if((!preferences.contains("family_long")||(!preferences.contains("family_lat")))) familyDisplay = false;
            if(preferences.getAll().containsKey("family_long"))  familyLon = preferences.getFloat("family_long", (float)0);
            if(preferences.getAll().containsKey("family_lat"))  familyLat = preferences.getFloat("family_lat", (float)0);
        } catch(Exception e) {
            System.out.println("Parse error");
        }

        String house_label = preferences.getString("home_label", "");
        String friend_label = preferences.getString("friend_label", "");
        String family_label = preferences.getString("family_label", "");

        TextView house_label_view = findViewById(R.id.house_label_view);
        TextView friend_label_view = findViewById(R.id.friend_label_view);
        TextView family_label_view = findViewById(R.id.family_label_view);

        house_label_view.setText(house_label);
        friend_label_view.setText(friend_label);
        family_label_view.setText(family_label);

        ImageView house_icon = findViewById(R.id.house);
        ImageView friend_icon = findViewById(R.id.friend);
        ImageView family_icon = findViewById(R.id.family);

        if(!houseDisplay) {
            house_icon.setVisibility(INVISIBLE);
            house_label_view.setVisibility(INVISIBLE);
        }

        if(!friendDisplay) {
            friend_icon.setVisibility(INVISIBLE);
            friend_label_view.setVisibility(INVISIBLE);
        }

        if(!familyDisplay) {
            family_icon.setVisibility(INVISIBLE);
            family_label_view.setVisibility(INVISIBLE);
        }
    }

    void updateLocation() {
        locationService.getLocation().observe(this, location -> {
            lat = location.first;
            lon = location.second;
            loc.latitude = location.first;
            loc.longitude = location.second;
            api.putLocation(loc);
            setImageDirections();
        });
    }

    LiveData<Location> friendLoc;
    void setFriendLocation() {
        var viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        friendLoc = viewModel.getNote("ranatest5");
        friendLoc.observe(this, this::onLocChanged);
    }

    public void onLocChanged(Location changeLoc) {
        friendLat = changeLoc.latitude;
        friendLon = changeLoc.longitude;
    }

    void setImageDirections() {
        ImageView friend = findViewById(R.id.friend);
        renderImage(friend, friendLat, friendLon);
        ImageView family = findViewById(R.id.family);
        renderImage(family, familyLat, familyLon);
        ImageView house = findViewById(R.id.house);
        renderImage(house, houseLat, houseLon);
        stackIcons();
    }

    void renderImage(ImageView image, double otherLat, double otherLon) {
        double degrees = angleFromCoordinate(lat, lon, otherLat, otherLon);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        layoutParams.circleAngle = (float)(degrees-orient*(180 / Math.PI));
        image.setLayoutParams(layoutParams);
    }

    void stackIcons() {
        ImageView house = findViewById(R.id.house);
        ConstraintLayout.LayoutParams houseLayoutParams = (ConstraintLayout.LayoutParams) house.getLayoutParams();
        ImageView friend = findViewById(R.id.friend);
        ConstraintLayout.LayoutParams friendLayoutParams = (ConstraintLayout.LayoutParams) friend.getLayoutParams();
        ImageView family = findViewById(R.id.family);
        ConstraintLayout.LayoutParams familyLayoutParams = (ConstraintLayout.LayoutParams) family.getLayoutParams();
        if(Math.abs(houseLayoutParams.circleAngle - friendLayoutParams.circleAngle) < 20) {
            friendLayoutParams.circleRadius = houseLayoutParams.circleRadius - 100;
        } else friendLayoutParams.circleRadius = houseLayoutParams.circleRadius;
        if(Math.abs(friendLayoutParams.circleAngle - familyLayoutParams.circleAngle) < 20) {
            familyLayoutParams.circleRadius = friendLayoutParams.circleRadius - 100;
        } else if(Math.abs(houseLayoutParams.circleAngle - familyLayoutParams.circleAngle) < 20) {
            familyLayoutParams.circleRadius = houseLayoutParams.circleRadius - 100;
        } else familyLayoutParams.circleRadius = friendLayoutParams.circleRadius;
        house.setLayoutParams(houseLayoutParams);
        friend.setLayoutParams(friendLayoutParams);
        family.setLayoutParams(familyLayoutParams);
    }

    public static double angleFromCoordinate(double lat1, double long1, double lat2,
                                       double long2) {
        double brng = Math.atan2(lat1 - lat2, long1 - long2);
        brng = brng * (180 / Math.PI);
        brng = 360 - brng;
        return (brng - 90 + 360) % 360;
    }
}
