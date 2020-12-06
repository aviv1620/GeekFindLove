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
import android.widget.Button;

import com.example.geekfindlove.dummy.DummyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class QuestionsFragment1 extends Fragment implements ValueEventListener, View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<QuestionsInformation> ans;
    private FirebaseDatabase mDatabase; // creating database object
    private DatabaseReference dbRef; // creating reference to our database
    private QuestionsRecyclerViewAdapter q_recycle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionsFragment1() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestionsFragment1 newInstance(int columnCount) {
        QuestionsFragment1 fragment = new QuestionsFragment1();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ans = new ArrayList<>(); // initializing
        q_recycle= new QuestionsRecyclerViewAdapter(ans);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mDatabase = FirebaseDatabase.getInstance();
        dbRef = mDatabase.getReference("questions");
        dbRef.addValueEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions1_list, container, false);
        //button
        Button button = (Button)view.findViewById(R.id.buttonSave);
        button.setOnClickListener(this);

        //recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(q_recycle);
        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) { // going through all the childresns of clients
            QuestionsInformation questionInformation = child.getValue(QuestionsInformation.class);
            questionInformation.setId(child.getKey());
            ans.add(questionInformation);
        }
        q_recycle.setmValues(ans);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onClick(View v) {
        q_recycle.onSavePress();
    }
}