package com.example.geekfindlove;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class AdminQuestionFragment extends Fragment implements ValueEventListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private ArrayList<QuestionsInformation> ans;
    private AdminQuestionRecyclerViewAdapter q_recycle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AdminQuestionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AdminQuestionFragment newInstance(int columnCount) {
        AdminQuestionFragment fragment = new AdminQuestionFragment();
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

        ans = new ArrayList<>(); // initializing
        q_recycle= new AdminQuestionRecyclerViewAdapter(ans);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference("questions");
        // each time we come to this fragment the eventValueListener will be called.
        dbRef.addValueEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_question_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(q_recycle);
        }
        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ans.clear();
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
}