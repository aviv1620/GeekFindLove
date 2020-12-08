package com.example.geekfindlove;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geekfindlove.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 */
public class MatchingRecyclerViewAdapter extends RecyclerView.Adapter<MatchingRecyclerViewAdapter.ViewHolder> {

    private final List<MatchingInformation> mValues;

    public MatchingRecyclerViewAdapter(List<MatchingInformation> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_matching_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MatchingInformation mi =  mValues.get(position);
        UserInformation u = mi.getDstDetail();


        holder.mItem = mi;
        holder.MatchingName.setText(u.getFn() + " " + u.getLn());
        String p = mi.getPercent() + "%";
        holder.MatchingPercent.setText(p);
        //holder.showNumber TODO show the number.
        //holder.avatar TODO load the avatar.

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView MatchingName;
        public final TextView MatchingPercent;
        public final Button showNumber;
        public final ImageView avatar;

        public MatchingInformation mItem;

        public ViewHolder(View view) {
            super(view);

            MatchingName = (TextView) view.findViewById(R.id.textViewMatchingName);
            MatchingPercent = (TextView) view.findViewById(R.id.textViewMatchingPercent);
            showNumber = (Button)view.findViewById(R.id.buttonMatchingshowNumber);
            avatar =  (ImageView)view.findViewById(R.id.imageViewMatchingAvatar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + MatchingName.getText() + "'";
        }
    }
}