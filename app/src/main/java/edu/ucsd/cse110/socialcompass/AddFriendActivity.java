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

//    ArrayList<String> arr = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
//        loadUIDs();
    }


    public void onAddFriendSubmitBtnClicked(View view) {
        boolean addSuccessful = true;

        //get entered uid
        EditText addFriendView = findViewById(R.id.enterFriendID);

        //add friend with associated uid
        saveUIDs(); //save uid as a param

        //alert that friend is added if successfully added
        if(addSuccessful) Utilities.showAlert(this,"Friend Added Successfully", "Add another friend by entering their uid");
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

    public void loadUIDs() {
        SharedPreferences preferences = getSharedPreferences("UIDs", MODE_PRIVATE);
        String UID_val = preferences.getString("UID_label", "");
        TextView UID_val_view = findViewById(R.id.enterFriendID);
        if(preferences.contains("UID_val")) UID_val_view.setText(UID_val);
//        UID_val_view.setText(UID_val);
    }

    public void saveUIDs() {
        SharedPreferences preferences = getSharedPreferences("UIDs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        TextView UID_val_view = findViewById(R.id.enterFriendID);
        Set<String> existingUIDs = new HashSet<>(preferences.getStringSet("UIDs", new HashSet<>()));

        existingUIDs.add(UID_val_view.getText().toString());

        editor.putStringSet("UIDs", existingUIDs);

//        editor.putString("UID_label", UID_val_view.getText().toString());
//        arr.add(UID_val_view.getText().toString());
        //add an array here
        //TextView orientation_test = findViewById(R.id.orientation_test);
        //if(!orientation_test.getText().toString().equals("")) editor.putFloat("orientation", Float.parseFloat(orientation_test.getText().toString()));

        editor.apply();
    }

    public void onContinueButtonClicked(View view) {
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }
}