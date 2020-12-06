package com.example.geekfindlove.dummy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DummyActivity extends AppCompatActivity implements ValueEventListener {
    private final static  String TAG = "DummyActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
        ;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //dummy not test if user is null

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        //aliceID is yzzkB3SkT1ec051rwygBuss0wIs2
        //bobID is AzLZ5AzB8ARXhJtgsSNFIM4Xk6d2
        //cerolID ids 2L6erYK5W2fpWxT60O8RGuZJife2

        //if bob is connected.

        //bed is ascending order and i can't cenge to descending order order.
        Query q = mDatabase.getReference("/matching/AzLZ5AzB8ARXhJtgsSNFIM4Xk6d2").orderByChild("Percent").limitToLast(1);
        //in old order.
        //Query q = mDatabase.getReference("/matching").orderByChild("userIdSrc").equalTo("AzLZ5AzB8ARXhJtgsSNFIM4Xk6d2");


        q.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG,"childs");
        for (DataSnapshot child:dataSnapshot.getChildren())
            Log.d(TAG,child.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
