package com.example.roomateproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private FirebaseDatabase database;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        sharedPref = getSharedPreferences("key", MODE_PRIVATE);

        getSupportActionBar().hide();

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);

            //DatabaseReference myRef = database.getReference("message");
            //myRef.setValue("Test");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TAG"," blerg outside");

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            Log.d("TAG"," blerg one more step");
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //id is essentially an over glorified boolean. Need to change this before submission
                final String[] id = new String[1];
                id[0] = null;

                final int[] homeID = new int[1];

                Log.d("TAG"," blerg anotha one");

                //Check the database to see if this is a first login
                DatabaseReference myRef = database.getReference("Mate");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Looks to see if the dataSnapshot is null or not
                        if(dataSnapshot.getValue() != null){
                            //If it is not null that means it's a hashmap, so it maps the map.
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                            //Loops through the map and if it finds the proper id it sets the id variable to the id
                            for(Map.Entry<String, Object> entry: map.entrySet()){
                                if(Objects.equals(entry.getKey(), user.getUid())){
                                    id[0] = entry.getKey();
                                }
                            }
                        }
                        else{
                            id[0] = dataSnapshot.getValue(String.class);
                        }

                        //Checks to see if the user exists in the db already.
                        if(id[0] != null) //If user does exist in the db
                        {

                            DatabaseReference myRef = database.getReference("Mate");
                            final boolean[] hasHouse = {false};

                            //Check to see if they have a house associated.
                             myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Makes sure there is a user to be grabbed.
                                if(dataSnapshot.getValue() != null){
                                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                    //Check if the houseId is null on the user.
                                    if((map.get("houseID") != null)){
                                        hasHouse[0] = true;

                                        Log.d("Tag", String.valueOf(homeID[0]));
                                    }
                                }

                            }

                            //This method does nothing and by doing nothing the hasHouse stays false
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                            });


                             if(hasHouse[0]) //If there is a house associated redirects to PersonalActivity
                             {
                                 Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                                 startActivityForResult(intent, 2);
                             }
                             else //else redirect to NoHouseActivity
                             {
                                 Intent intent = new Intent(MainActivity.this, NoHouseActivity.class);
                                 startActivityForResult(intent, 2);
                             }
                        }
                        else //else add the user into the Db then send them to set their name
                        {
                            Mate currentMate = new Mate();

                            currentMate.setUserID(user.getUid());

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userID", user.getUid());
                            editor.putString("displayName", user.getDisplayName());
                            editor.commit();

                            //Goto the newUserActivity
                            Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                            startActivityForResult(intent, 2);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //id[0] = null;
                        Log.d("TAG"," blerg");
                    }
                });







            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...

                Log.d("TAG"," blerg failed");
            }
        }
    }
}