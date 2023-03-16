package edu.ucsd.cse110.socialcompass;

import static android.view.View.INVISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;
import edu.ucsd.cse110.socialcompass.model.LocationRepository;
import edu.ucsd.cse110.socialcompass.model.LocationViewModel;

public class CompassActivity extends AppCompatActivity {
    public LocationService locationService;
    public OrientationService orientationService;

    Location loc;
    LocationAPI api;

    String UID;
    String label;
    String priv_key = "notouch";

    double lat, lon;
    double orient = 0;

    String[] uids;

    ArrayList<LiveData<Location>> liveLocs;
    ArrayList<Location> locs;
    ArrayList<TextView> friends;
    double zoom_max = 10;

    int constraint_size = 500;

    double constraintZoomRatio = constraint_size/zoom_max;

    boolean houseDisplay, friendDisplay, familyDisplay;
    double friendLat, friendLon;
    double houseLat, houseLon;
    double familyLat, familyLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        //load in name and UID from shared preferences
        SharedPreferences preferencesUID = getSharedPreferences("UID", MODE_PRIVATE);
        SharedPreferences preferencesName = getSharedPreferences("Name", MODE_PRIVATE);
        UID = preferencesUID.getString("UID",null);
        label = preferencesName.getString("enter_name",null);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);

        readLabelFromSP();

        loc = new Location(UID);
        api = LocationAPI.provide();
        loc.label = label;
        loc.private_code = priv_key;

        getLocations();
        updateLocation();
        if(!mockOrientationWithBox()) updateOrientation();

        getUIDs();

        setUpUIDs();
    }

    public void setUpUIDs() {
        ConstraintLayout compass = findViewById(R.id.compass);

        ImageView friend = findViewById(R.id.friend);

        liveLocs = new ArrayList<>();
        locs = new ArrayList<>();
        friends = new ArrayList<>();

        for(int i=0; i<uids.length; i++) {
            LocationRepository repo = new LocationRepository();
            Log.i("ADDED", uids[i]+"");
            LiveData<Location> liveLoc = repo.getRemote(uids[i]);
            Location l = new Location(uids[i]);
            liveLoc.observe(this, this::onLocChanged);
            liveLocs.add(liveLoc);
            locs.add(l);
        }

        for(int i=0; i<uids.length; i++) {
            TextView temp = new TextView(this);
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.dot_icon2);
            dot.setScaleType(ImageView.ScaleType.FIT_CENTER);
            temp.setText(uids[i]);
            temp.setShadowLayer((float) 1.6, (float) -1.5, (float) 1.3, -1);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) friend.getLayoutParams();
            ConstraintLayout.LayoutParams copy = new ConstraintLayout.LayoutParams((ViewGroup.LayoutParams)params);

            compass.addView(dot);
            compass.addView(temp);
            copy.circleConstraint = compass.getId();
            copy.circleAngle = i*50;
            copy.circleRadius = 500;
            copy.height = 50;
            copy.width = 250;

            temp.setLayoutParams(copy);
            dot.setLayoutParams(copy);

            friends.add(temp);
        }

        friend.setVisibility(INVISIBLE);
    }
    public void readLabelFromSP() {
        SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
        label = preferences.getString("enter_name", "ERROR");
    }

    public void updateAllLocs() {
        for(int i=0; i<locs.size(); i++) {
            updateLoc(i);
        }
    }
    public void updateLoc(int i) {
        TextView currView = friends.get(i);
        Location currLoc = locs.get(i);
        currView.setText(currLoc.label);
        renderText(currView, currLoc.latitude, currLoc.longitude);
    }

    void renderText(TextView text, double otherLat, double otherLon) {
        double degrees = angleFromCoordinate(lat, lon, otherLat, otherLon);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) text.getLayoutParams();
        layoutParams.circleAngle = (float)(degrees-orient*(180 / Math.PI));
        // TODO: add this line
        double locDist = distInMiles(otherLat, otherLon, lat, lon);
        if (locDist > zoom_max){
            layoutParams.circleRadius = 500;
            text.setVisibility(View.INVISIBLE);
        } else {
            layoutParams.circleRadius = (int) (locDist * constraintZoomRatio);
            text.setVisibility(View.VISIBLE);
        }
        text.setLayoutParams(layoutParams);
    }

    public void onLocChanged(Location changeLoc) {
        for(int i=0; i<locs.size(); i++) {
            if(locs.get(i).UID.equals(changeLoc.UID)) {
                locs.set(i, changeLoc);
                Log.i("GOT LOC", changeLoc.UID);
                updateLoc(i);
            }
        }
    }



    void updateOrientation() {
        orientationService.getOrientation().observe(this, orientation -> {
            orient = orientation;
            setImageDirections();
            updateAllLocs();
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

    public void getUIDs() {
        SharedPreferences preferences = getSharedPreferences("UIDs", MODE_PRIVATE);
        Set<String> uidSet = preferences.getStringSet("UIDs", new HashSet<>());

        uids = new String[uidSet.size()];
        int c=0;
        for(String s : uidSet) {
            uids[c++] = s;
        }
    }

    public void getLocations(){
        SharedPreferences preferences = getSharedPreferences("Locations", MODE_PRIVATE);

        // for now, just have them be set to invisible
        houseDisplay=false;
        friendDisplay=false;
        familyDisplay=false;

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
            updateAllLocs();
            setImageDirections();
            //renderDistances();
        });
    }

    void setImageDirections() {
        ImageView friend = findViewById(R.id.friend);
        renderImage(friend, friendLat, friendLon);
        ImageView family = findViewById(R.id.family);
        renderImage(family, familyLat, familyLon);
        ImageView house = findViewById(R.id.house);
        renderImage(house, houseLat, houseLon);
        //stackIcons();
    }

    void renderImage(ImageView image, double otherLat, double otherLon) {
        double degrees = angleFromCoordinate(lat, lon, otherLat, otherLon);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        layoutParams.circleAngle = (float)(degrees-orient*(180 / Math.PI));
        image.setLayoutParams(layoutParams);
    }

    public static double latDistInMiles(double latDist, double curLat){
        return Math.abs(latDist - curLat) * 69;
    }

    public static double lonDistInMiles(double lonDist, double curLon){
        return Math.abs(lonDist - curLon) * 54.6;
    }

    public static double distInMiles(double latDist, double lonDist, double curLat, double curLon){
        return Math.sqrt(Math.pow(lonDistInMiles(lonDist, curLon), 2) + Math.pow(latDistInMiles(latDist, curLat), 2));
    }

    void increaseZoom() {
        // stub for now;
    }

    void decreaseZoom() {
        // stub for now;
    }

    void setZoomToDefault() {
        // stub for now;
    }

    void renderDistances() {
        double constraintToZoomRatio = constraint_size/zoom_max;

        ImageView house = findViewById(R.id.house);
        ConstraintLayout.LayoutParams houseLayoutParams = (ConstraintLayout.LayoutParams) house.getLayoutParams();
        ImageView friend = findViewById(R.id.friend);
        ConstraintLayout.LayoutParams friendLayoutParams = (ConstraintLayout.LayoutParams) friend.getLayoutParams();
        ImageView family = findViewById(R.id.family);
        ConstraintLayout.LayoutParams familyLayoutParams = (ConstraintLayout.LayoutParams) family.getLayoutParams();

        double houseDist = distInMiles(houseLat, houseLon, lat, lon);
        double friendDist = distInMiles(friendLat, friendLon, lat, lon);
        double familyDist = distInMiles(familyLat, familyLon, lat, lon);
        Log.i("OUR_LONG_LAT", String.valueOf(lat) + " " + String.valueOf(lon));

        if (houseDist > zoom_max){
            houseLayoutParams.circleRadius = 500;
        } else {
            houseLayoutParams.circleRadius = (int) (houseDist * constraintToZoomRatio);
        }
        //Log.i("HOUSE_LONG_LAT", String.valueOf(houseLat) + " " + String.valueOf(houseLon));
        //Log.i("HOUSE_DIST", String.valueOf(houseDist));

        if (friendDist > zoom_max){
            friendLayoutParams.circleRadius = 500;
        } else {
            friendLayoutParams.circleRadius = (int) (friendDist * constraintToZoomRatio);
        }
        //Log.i("FRIEND_LONG_LAT", String.valueOf(friendLat) + " " + String.valueOf(friendLon));

        if (familyDist > zoom_max){
            familyLayoutParams.circleRadius = 500;
        } else {
            familyLayoutParams.circleRadius = (int) (familyDist * constraintToZoomRatio);
        }
        //Log.i("FAMILY_LONG_LAT", String.valueOf(familyLat) + " " + String.valueOf(familyLon));
        //Log.i("FAMILY_DIST", String.valueOf(familyDist));

        house.setLayoutParams(houseLayoutParams);
        friend.setLayoutParams(friendLayoutParams);
        family.setLayoutParams(familyLayoutParams);
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
