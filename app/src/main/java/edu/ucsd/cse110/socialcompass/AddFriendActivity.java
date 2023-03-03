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
        EditText addFriendView = findViewById(R.id.enterFriendID);
        String uid = addFriendView.getText().toString();


    }
}