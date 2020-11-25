package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
    }

    //Temporary code FIXME
    private void Temporary(){
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
}