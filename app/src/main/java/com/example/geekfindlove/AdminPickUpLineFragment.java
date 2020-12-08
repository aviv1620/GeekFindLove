package com.example.geekfindlove;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geekfindlove.dummy.DummyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class AdminPickUpLineFragment extends Fragment implements ValueEventListener {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "AdminPickUpLine";

    private int mColumnCount = 1;
    private ArrayList<PickUpLineInformation> pickUpLineList;
    private AdminPickUpLineRecyclerViewAdapter recyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AdminPickUpLineFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AdminPickUpLineFragment newInstance(int columnCount) {
        AdminPickUpLineFragment fragment = new AdminPickUpLineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        //
        pickUpLineList = new ArrayList<>(); // initializing
        recyclerViewAdapter= new AdminPickUpLineRecyclerViewAdapter(pickUpLineList);
        //firebase
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference("pickUpLine");
        // each time we come to this fragment the eventValueListener will be called.
        dbRef.addValueEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_pickupline_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        pickUpLineList.clear();
        for(DataSnapshot child:dataSnapshot.getChildren()){
            String value = child.getValue(String.class);
            String key = child.getKey();
            PickUpLineInformation pickUpLine = new PickUpLineInformation(key,value);
            pickUpLineList.add(pickUpLine);
            Log.d(TAG,pickUpLine.toString());
        }
        recyclerViewAdapter.setmValues(pickUpLineList);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}