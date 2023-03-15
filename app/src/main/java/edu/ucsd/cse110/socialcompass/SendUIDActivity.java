package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SendUIDActivity extends AppCompatActivity {
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_uid);

        //TODO:get UID from shared preferences
        //mocking for right now with random uid
        uid = "sidtest123";
        TextView uidBox = findViewById(R.id.uidCodeView);
        uidBox.setText(uid);
    }


    public void onCopyBtnClicked(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("uid", this.uid);
        clipboard.setPrimaryClip(clip);
        Utilities.showAlert(this,"UID copied", "Your UID has been copied to clipboard.");
    }


    public void onNextBtnClicked(View view) {
        Intent intent = new Intent(this,AddFriendActivity.class);
        startActivity(intent);
    }
}