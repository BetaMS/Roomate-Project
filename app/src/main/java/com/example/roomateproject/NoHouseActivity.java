package com.example.roomateproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NoHouseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_house);

        getSupportActionBar().hide();
    }


    public void onGoClick(View view){
        EditText editText = findViewById(R.id.houseCodeEditText);
        String textValue = editText.getText().toString();

        //Check to see if the code is 5 digits
        if(textValue.toCharArray().length == 5) {
            //Adds it to the user and redirects to PersonalActivity

        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "House Code needs to be 5 digits!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //Else if it is the wrong format a toast appears telling them

    }

    public void onCreateHouseClick(View view){
        //Redirects to NewHouseActivity
    }
}