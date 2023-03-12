package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EnterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
    }

    public void onSubmitClicked(View view) {
        saveName();
        if (atLeastOneLocationExists()) {
            Intent intent = new Intent(this, CompassActivity.class);// New activity
            startActivity(intent);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(AddLocationsActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Must enter at least 1 location");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
    }

    public void saveName() {
        SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();

        TextView enter_name = findViewById(R.id.enter_name_edittext);
        editor.putString("enter_name", enter_name.getText().toString());

        editor.apply();
    }


}