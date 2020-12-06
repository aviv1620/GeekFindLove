package com.example.geekfindlove;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.geekfindlove.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.ViewHolder> {

    private List<QuestionsInformation> mValues;

    public void setmValues(List<QuestionsInformation> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged(); // notifying android that we changed the list
    }

    public QuestionsRecyclerViewAdapter(List<QuestionsInformation> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // Context context;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_questions1, parent, false);
       // context=parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      //  holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
        holder.questionFB.setText(mValues.get(position).getQuestion());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(holder.mView.getContext(), android.R.layout.simple_spinner_item, mValues.get(position).getAnswers());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        holder.answeresFB.setAdapter(dataAdapter);
        Log.v("Sivan", "we are here");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DummyItem mItem;
        public TextView questionFB;
        public Spinner answeresFB;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            questionFB=(TextView)view.findViewById(R.id.QuestionFB);
            answeresFB=(Spinner)view.findViewById(R.id.AnsweresFB);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + questionFB.getText() + "'";
        }
    }
}