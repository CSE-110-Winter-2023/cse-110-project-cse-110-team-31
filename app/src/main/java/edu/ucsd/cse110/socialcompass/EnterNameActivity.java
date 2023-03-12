package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class EnterNameActivity extends AppCompatActivity {

    // Possible TODO: on enter in the edit text click text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
    }

//    public void onEnterListener() {
//        addKeyListener(new KeyAdapter()
//        {
//            public void keyPressed(KeyEvent evt)
//            {
//                if(evt.getKeyCode() == KeyEvent.VK_ENTER)
//                {
//                    System.out.println("Pressed");
//                }
//            }
//        });
//
//    }


    public void onSubmitClicked(View view) {
        saveName();
//        if (ifNameExists()) {
        Intent intent = new Intent(this, CompassActivity.class);// TODO: change to 2.2 wireframe class
        startActivity(intent);
//        } else {
//            AlertDialog alertDialog = new AlertDialog.Builder(/*TODO: create the 2.2 wireframe class*/.this).create();
//            alertDialog.setTitle("Alert");
//            alertDialog.setMessage("Must enter at least 1 location");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    (dialog, which) -> dialog.dismiss());
//            alertDialog.show();
//        }
    }

    // Possible TODO: check for if name is not entered or bad values are entered (weird stuff etc..)
    public void saveName() {
        SharedPreferences preferences = getSharedPreferences("Name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();

        TextView enter_name = findViewById(R.id.enter_name_edittext);
        editor.putString("enter_name", enter_name.getText().toString());

        editor.apply();
    }


//    @Override
//    public int getInputType() {
//        return 0;
//    }
//
//    @Override
//    public boolean onKeyDown(View view, Editable editable, int i, KeyEvent keyEvent) {
//        return false;
//    }
//
//    @Override
//    public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
//        return false;
//    }
//
//    @Override
//    public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
//        return false;
//    }
//
//    @Override
//    public void clearMetaKeyState(View view, Editable editable, int i) {

//}
}