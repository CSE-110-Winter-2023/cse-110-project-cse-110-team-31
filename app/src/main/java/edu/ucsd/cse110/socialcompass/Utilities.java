package edu.ucsd.cse110.socialcompass;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import edu.ucsd.cse110.socialcompass.model.Location;
import edu.ucsd.cse110.socialcompass.model.LocationAPI;


public class Utilities {
    public static void showAlert(Activity activity, String title, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok", (dialog,id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static String generateUID(){
        String uniqueID = null;
        LocationAPI locAPI = new LocationAPI();

        while(uniqueID == null){
            try {
                //generate a random UID
                uniqueID = UUID.randomUUID().toString();
                Location checkIfExists = locAPI.getLocation(uniqueID);
                Log.i("GEN_UID","GENERATED: " + uniqueID);
                //check if it already exists
                if(checkIfExists.label != null){
                    //if so try again
                    uniqueID = null;
                    continue;
                }
            } catch (Exception e){
                //if not found, then use this UID
                Log.i("GEN_UID","FINAL: " + uniqueID);
                break;
            }
        }

        Log.i("GEN_UID", "USER_UID: " + uniqueID);
        return uniqueID;
    }

    public static void clearNameUID(Activity act){
        SharedPreferences p = act.getSharedPreferences("Name",MODE_PRIVATE);
        SharedPreferences f = act.getSharedPreferences("UID",MODE_PRIVATE);
        SharedPreferences.Editor pe = p.edit();
        SharedPreferences.Editor fe = f.edit();
        pe.remove("enter_name");
        fe.remove("UID");
        pe.apply();
        fe.apply();
    }
}
