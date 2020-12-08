package com.example.geekfindlove;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.geekfindlove.dummy.DummyContent.DummyItem;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AdminPickUpLineRecyclerViewAdapter extends RecyclerView.Adapter<AdminPickUpLineRecyclerViewAdapter.ViewHolder> {

    private List<PickUpLineInformation> mValues;

    public AdminPickUpLineRecyclerViewAdapter(List<PickUpLineInformation> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_pickupline_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getValue());

        holder.databaseKey = mValues.get(position).getKey();
        holder.mDeleteButton.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setmValues(ArrayList<PickUpLineInformation> pickUpLineList) {
        this.mValues = mValues;
        notifyDataSetChanged(); // notifying android that we changed the list,refresh the list that was empty at first.
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mContentView;
        public PickUpLineInformation mItem;
        public Button mDeleteButton;

        public String databaseKey;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mDeleteButton = (Button) view.findViewById(R.id.button3);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            mDatabase.getReference("pickUpLine").child(databaseKey).removeValue();
        }
    }
}