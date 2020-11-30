package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// importing the necessary libraries to manage fragments :
//import android.app.Fragment;
import androidx.fragment.app.Fragment;

//AppCompatActivity,
public class UserActivity extends AppCompatActivity {
    private static String TAG = "UserActivity";

    //TODO tree in powerpoint to real time firebasee
    //TODO add fregments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "UserActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Temporary();
        default_fragment_when_login(); // calling this function in order to direct us to profile page as we log in (as default)

    }

    public void default_fragment_when_login() {
        Fragment fragment;
        fragment = new profile_fragment();
        FragmentManager fm = getSupportFragmentManager();
        // using ft means that we want to replace the current fragment to the new one that the user selected
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.general_fragment, fragment);
        ft.commit();
    }

    //Temporary code FIXME
    private void Temporary() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("questions/IDquestions1/Answer1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // in this function we will write how to change between certain fragments
    // according to the user decision
    public void change_fragments(View view) {
        Fragment fragment;
        // first we will check which button is being clicked :
        // when we know which button is being clicked, we will want to assign fragment object its right class
        // for example for profile_id we want the class profile_fragment
        if (view == findViewById(R.id.profile_id)) {
            fragment = new profile_fragment();
            FragmentManager fm = getSupportFragmentManager();
            // using ft means that we want to replace the current fragment to the new one that the user selected
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.general_fragment, fragment);
            ft.commit();
        }
        if (view == findViewById(R.id.questions_id)) {
            fragment = new questions_fragment();
            FragmentManager fm = getSupportFragmentManager();
            // using ft means that we want to replace the current fragment to the new one that the user selected
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.general_fragment, fragment);
            ft.commit();
        }
    }
}