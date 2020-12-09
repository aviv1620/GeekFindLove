package com.example.geekfindlove;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PickUpLineRecyclerViewAdapter extends RecyclerView.Adapter<PickUpLineRecyclerViewAdapter.ViewHolder> {

    private  List<PickUpLineInformation> mValues;

    public PickUpLineRecyclerViewAdapter(List<PickUpLineInformation> items) {
        mValues = items;
    }

    public void setmValues(List<PickUpLineInformation> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged(); // notifying android that we changed the list,refresh the list that was empty at first.
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pickupline_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).getValue());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


    public static  List<String> DummyPickUpLine(){
        List<String> ans = new ArrayList<>();
        ans.add("line a");
        ans.add("line b");
        ans.add("line c");
        ans.add("line s");
        return ans;
    }
}