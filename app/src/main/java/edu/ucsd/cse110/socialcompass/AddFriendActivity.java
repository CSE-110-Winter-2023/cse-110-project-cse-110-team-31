package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        String uid = addFriendView.getText().toString();

        //add friend with associated uid

        //alert that friend is added if successfully added
        if(addSuccessful) Utilities.showAlert(this,"Friend Added Successfully", "Add another friend by entering their uid");
        else Utilities.showAlert(this,"Unable to Add Friend", "Error while adding user with entered UID");

        //reset text box
        addFriendView.setHint("Enter another UID");
        addFriendView.setText("");


    }
}