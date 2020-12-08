package com.example.geekfindlove;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.geekfindlove.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AdminQuestionRecyclerViewAdapter extends RecyclerView.Adapter<AdminQuestionRecyclerViewAdapter.ViewHolder> {

    private List<QuestionsInformation> mValues;

    public AdminQuestionRecyclerViewAdapter(List<QuestionsInformation> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_question_item, parent, false);
        return new ViewHolder(view);
    }

    public void setmValues(List<QuestionsInformation> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged(); // notifying android that we changed the list,refresh the list that was empty at first.
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String str = mValues.get(position).getQuestion();
        for(String ans:mValues.get(position).getAnswers())
            str += "\n\t[*] " + ans;

        holder.mContentView.setText(str);
        //TODO delete button

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public QuestionsInformation mItem;
        public Button mButtonDelete;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mButtonDelete = (Button) view.findViewById(R.id.buttonDelete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}