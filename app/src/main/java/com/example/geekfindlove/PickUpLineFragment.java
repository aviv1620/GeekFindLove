package com.example.geekfindlove;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class PickUpLineFragment extends Fragment implements ValueEventListener {


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<PickUpLineInformation> ans;
    private FirebaseDatabase mDatabase; // creating database object
    private DatabaseReference dbRef; // creating reference to our database
    private PickUpLineRecyclerViewAdapter p_recycle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PickUpLineFragment() {
    }

    @SuppressWarnings("unused")
    public static PickUpLineFragment newInstance(int columnCount) {

        PickUpLineFragment fragment = new PickUpLineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ans = new ArrayList<>(); // initializing
        p_recycle= new PickUpLineRecyclerViewAdapter(ans);

        mDatabase = FirebaseDatabase.getInstance();
        dbRef = mDatabase.getReference("pickUpLine");
        // each time we come to this fragment the eventValueListener will be called.
        dbRef.addValueEventListener(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickupline_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //recyclerView.setAdapter(new PickUpLineRecyclerViewAdapter(PickUpLineRecyclerViewAdapter.DummyPickUpLine()));
            recyclerView.setAdapter(p_recycle);
        }
        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        ans.clear();
        for (DataSnapshot child : dataSnapshot.getChildren()) { // going through all the childresns of clients
            String value = child.getValue(String.class);
            PickUpLineInformation pickUpLineInformation = new PickUpLineInformation(child.getKey(),value);
         //   pickUpLineInformation.setKey(child.getKey());
            ans.add(pickUpLineInformation);
        }
        p_recycle.setmValues(ans);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}