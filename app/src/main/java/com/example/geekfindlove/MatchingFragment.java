package com.example.geekfindlove;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.geekfindlove.dummy.DummyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MatchingFragment extends Fragment implements ValueEventListener, AdapterView.OnItemSelectedListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    // add spinners
    private Spinner location;
    private Spinner age;
    private ArrayList<MatchingInformation> matchingList;
    private MatchingRecyclerViewAdapter matchingRecyclerViewAdapter;
    private int minAge=18;
    private int maxAge=60;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchingFragment() {
    }


    @SuppressWarnings("unused")
    public static MatchingFragment newInstance(int columnCount) {
        MatchingFragment fragment = new MatchingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchingList = new ArrayList<MatchingInformation>();
        matchingRecyclerViewAdapter = new MatchingRecyclerViewAdapter(matchingList);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }



        //FirebaseDatabase
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference("UserAnswer");
        dbRef.addValueEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matching_item_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();

        age = (Spinner)view.findViewById(R.id.ageSpinner);
        location = (Spinner)view.findViewById(R.id.locationSpinner);
        age.setOnItemSelectedListener(this);
        location.setOnItemSelectedListener(this);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerView.setAdapter(matchingRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        matchingList.clear();

        // if i dont pick age . it will show all the users.
        if (!age.getSelectedItem().toString().equals("Age")) {
            String[] splitAge = age.getSelectedItem().toString().split("-");
            minAge = Integer.parseInt(splitAge[0]);
            maxAge = Integer.parseInt(splitAge[1]);
            // taking the location value from location Spinner

        }
        else{
            minAge=18;
            maxAge=60;
        }
        String locationn = location.getSelectedItem().toString();
        MatchingAlgorithmSingleton.getInstance().setFilters(minAge, maxAge, locationn);

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            UserAnswerInformation userAnswer = child.getValue(UserAnswerInformation.class);
            MatchingInformation matchingInformation = MatchingAlgorithmSingleton.getInstance().UserAnswerInformation_To_MatchingInformation(userAnswer);
            if (matchingInformation != null)
                matchingList.add(matchingInformation);
            /*
            now we will create a notification that once a match is added , we want the user to be notify
             */
        }
        // "byPrecent" anonymous class, in order to implement the comparator needed to sort the hashmap.
        Collections.sort(matchingList, byPrecent);
        //matchingList.sort(byPrecent);
        matchingRecyclerViewAdapter.setmValues(matchingList);
        // converting the spinner age to two int varaibels.



    }

    // "byPrecent" lambda, in order to implement the comparator needed to sort the hashmap.
    Comparator<MatchingInformation> byPrecent = (mi_a, mi_b) -> mi_b.getPercent() - mi_a.getPercent();

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //FirebaseDatabase
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference("UserAnswer");
        dbRef.removeEventListener(this);
        dbRef.addValueEventListener(this);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


