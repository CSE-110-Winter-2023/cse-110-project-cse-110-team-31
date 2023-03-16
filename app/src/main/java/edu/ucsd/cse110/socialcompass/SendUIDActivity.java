package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SendUIDActivity extends AppCompatActivity {
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_uid);

        generateAndSetUID();
    }

    public void generateAndSetUID() {
        //generate a random UID
        uid = Utilities.generateUID();

        //Store it into shared preferences
        SharedPreferences preferences = getSharedPreferences("UID", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("UID", uid);
        editor.apply();

        //get uid from shared preferences
        String user_id = preferences.getString("UID", null);

        //set the uid textview
        TextView uidBox = findViewById(R.id.uidCodeView);
        if(user_id != null) {
            uidBox.setText(uid);
        } else {
            Utilities.showAlert(this,"SYSTEM ERROR", "UNABLE TO FIND UID");
        }
    }

    public void onCopyBtnClicked(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("uid", this.uid);
        clipboard.setPrimaryClip(clip);
        Utilities.showAlert(this,"UID copied", "Your UID has been copied to clipboard.");
    }


    public void onContinueBtnClicked(View view) {
        Intent intent = new Intent(this,AddFriendActivity.class);
        startActivity(intent);
    }
}