package edu.ucsd.cse110.socialcompass;

import android.app.Activity;
import android.app.AlertDialog;


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
}
