package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class EnterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
        String user_name = preferences.getString("enter_name", null);

        // if name is already entered in shared preferences don't ask again
        if (user_name != null){
            Log.i("ENTER_NAME_ACT","NAME FOUND: " + user_name);
            nextPage();
        }

    }

    public void onSubmitClicked(View view) {
        saveName();
        nextPage();
    }

    public void saveName() {
        SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.putString("enter_name", getName());
        editor.apply();
    }

    public String getName() {
        TextView enter_name = findViewById(R.id.enter_name_edittext);
        return enter_name.getText().toString();
    }

    public void setName(String name){
        TextView enter_name = findViewById(R.id.enter_name_edittext);
        enter_name.setText(name);
    }

    public void nextPage(){
        // TODO: change AddFriendActivity to 2.2 wireframe class ExchangeFriendUIDActivity
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

}