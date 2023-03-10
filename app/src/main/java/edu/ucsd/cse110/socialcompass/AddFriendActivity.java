package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        loadUIDs();
    }


    public void onAddFriendSubmitBtnClicked(View view) {


        boolean addSuccessful = true;

        //get entered uid
        EditText addFriendView = findViewById(R.id.enterFriendID);
        String uid = addFriendView.getText().toString();

        //add friend with associated uid
        saveUIDs();

        //alert that friend is added if successfully added
        if(addSuccessful) Utilities.showAlert(this,"Friend Added Successfully", "Add another friend by entering their uid");
        else Utilities.showAlert(this,"Unable to Add Friend", "Error while adding user with entered UID");

        //reset text box
        addFriendView.setHint("Enter another UID");
        addFriendView.setText("");
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
        UID_val_view.setText(UID_val);
    }

    public void saveUIDs() {
        SharedPreferences preferences = getSharedPreferences("UIDs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        TextView UID_val_view = findViewById(R.id.enterFriendID);
        editor.putString("UID_label", UID_val_view.getText().toString());

        TextView orientation_test = findViewById(R.id.orientation_test);
        if(!orientation_test.getText().toString().equals("")) editor.putFloat("orientation", Float.parseFloat(orientation_test.getText().toString()));

        editor.apply();
    }



}