package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }


    public void onAddFriendSubmitBtnClicked(View view) {
        boolean addSuccessful = true;

        //get entered uid
        EditText addFriendView = findViewById(R.id.enterFriendID);

        //add friend with associated uid
        saveUIDs(); //save uid as a param

        //alert that friend is added if successfully added
        if(addSuccessful) Utilities.showAlert(this,"Friend Added Successfully", "Add another friend by entering their UID");
        else Utilities.showAlert(this,"Unable to Add Friend", "Error while adding user with entered UID");

        //reset text box
        addFriendView.setHint("Enter another UID");
        addFriendView.setText("");
    }

    public void onDeleteButtonClicked(View view) {
        boolean removeSuccessful = true;
        SharedPreferences preferences = getSharedPreferences("UIDs", MODE_PRIVATE);
        preferences.edit().clear().apply();
        if(removeSuccessful) Utilities.showAlert(this,"All Friends Removed Successfully", "You can add friends by entering their UIDs");
        else Utilities.showAlert(this,"Unable to delete", "Error while trying to delete UIDs");

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void saveUIDs() {
        SharedPreferences preferences = getSharedPreferences("UIDs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        TextView UID_val_view = findViewById(R.id.enterFriendID);
        Set<String> existingUIDs = new HashSet<>(preferences.getStringSet("UIDs", new HashSet<>()));

        existingUIDs.add(UID_val_view.getText().toString());

        editor.putStringSet("UIDs", existingUIDs);

        editor.apply();
    }

    public void onContinueButtonClicked(View view) {
        SharedPreferences preferences = getSharedPreferences("URL", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextView urlView = findViewById(R.id.mockUrl);
        editor.clear();
        if(!urlView.getText().toString().equals("")) {
            editor.putString("url", urlView.getText().toString());
        }

        editor.apply();

        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }
}