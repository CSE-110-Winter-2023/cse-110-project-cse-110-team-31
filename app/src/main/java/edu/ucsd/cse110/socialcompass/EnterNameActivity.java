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
//    private SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
//    private SharedPreferences.Editor editor = preferences.edit();

    // Possible TODO: on enter in the edit text click text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String user_name = preferences.getString("enter_name", null);

        if (user_name != null){
            Log.i("ENTER_NAME_ACT","NAME FOUND: " + user_name);
            nextPage();
        }

    }

    public void onSubmitClicked(View view) {
        saveName();
        nextPage();
    }

    // Possible TODO: check for if name is not entered or bad values are entered (weird stuff etc..)
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
        Intent intent = new Intent(this, CompassActivity.class);// TODO: change to 2.2 wireframe class
        startActivity(intent);
    }

}