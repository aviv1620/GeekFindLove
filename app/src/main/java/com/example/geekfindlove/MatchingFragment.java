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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class MatchingFragment extends Fragment implements ValueEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    // add spinners
    private Spinner location;
    private Spinner age;
    private ArrayList<MatchingInformation> matchingList;
    private MatchingRecyclerViewAdapter matchingRecyclerViewAdapter;


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
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            UserAnswerInformation userAnswer = child.getValue(UserAnswerInformation.class);
            MatchingInformation matchingInformation = MatchingAlgorithmSingleton.getInstance().UserAnswerInformation_To_MatchingInformation(userAnswer);
            if (matchingInformation != null)
                matchingList.add(matchingInformation);
        }
        // "byPrecent" anonymous class, in order to implement the comparator needed to sort the hashmap.
        Collections.sort(matchingList, byPrecent);
        //matchingList.sort(byPrecent);
        matchingRecyclerViewAdapter.setmValues(matchingList);
        // converting the spinner age to two int varaibels.
        String [] splitAge = age.getSelectedItem().toString().split("-");
        int minAge = Integer.parseInt(splitAge[0]);
        int maxAge = Integer.parseInt(splitAge[1]);
        // taking the location value from location Spinner
        String locationn = location.getSelectedItem().toString();

         MatchingAlgorithmSingleton.getInstance().setFilters(minAge,maxAge,locationn);

    }

    // "byPrecent" lambda, in order to implement the comparator needed to sort the hashmap.
    Comparator<MatchingInformation> byPrecent = (mi_a, mi_b) -> mi_b.getPercent() - mi_a.getPercent();

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}

