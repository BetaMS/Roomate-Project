package com.example.roomateproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    SharedPreferences sharedPref;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        sharedPref = getSharedPreferences("key", MODE_PRIVATE);
        editText = findViewById(R.id.nameEditText);

        editText.setText(sharedPref.getString("displayName", ""));
    }

    public void onClickContinue(View view) {
        //put that user in the database
        DatabaseReference myRef = database.getReference("Mate");

        myRef.child(sharedPref.getString("userID", "No userID")).child("name").setValue(editText.getText().toString());



        //Go back to the MainActivity
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}