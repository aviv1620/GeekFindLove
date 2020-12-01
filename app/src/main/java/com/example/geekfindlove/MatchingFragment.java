package com.example.geekfindlove;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geekfindlove.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MatchingFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

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

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matching_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MatchingRecyclerViewAdapter(DummyMatchingInformations()));
        }
        return view;
    }

    public static List<MatchingInformation> DummyMatchingInformations(){
        ArrayList<MatchingInformation> ans = new ArrayList<MatchingInformation>();

        MatchingInformation.DstDetail dd1 = new MatchingInformation.DstDetail("0000000001","alice a","Address");
        MatchingInformation mi1 = new MatchingInformation(90,"dstId2","srcId1",dd1);
        ans.add(mi1);

        MatchingInformation.DstDetail dd2 = new MatchingInformation.DstDetail("0000000002","Bob b","Address");
        MatchingInformation mi2 = new MatchingInformation(90,"dstId2","srcId1",dd2);
        ans.add(mi2);

        MatchingInformation.DstDetail dd3 = new MatchingInformation.DstDetail("0000000003","Carol b","https://en.wikipedia.org/wiki/Alice_and_Bob#Cast_of_characters");
        MatchingInformation mi3 = new MatchingInformation(90,"dstId3","srcId1",dd3);
        ans.add(mi3);

        return ans;
    }
}

