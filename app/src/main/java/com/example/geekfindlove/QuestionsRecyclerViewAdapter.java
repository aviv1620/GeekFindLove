package com.example.geekfindlove;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.geekfindlove.dummy.DummyContent.DummyItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "QuestionsRecycler";
    private List<QuestionsInformation> mValues;
    private HashMap<String,Integer> answer;

    public void setmValues(List<QuestionsInformation> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged(); // notifying android that we changed the list,refresh the list that was empty at first.
    }

    public QuestionsRecyclerViewAdapter(List<QuestionsInformation> items) {
        // initially we get an empty list, it will refresh after a sec,using the setmValues above.
        answer = new HashMap<>();
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Context context;
        // giving the one item layout and not the list here.
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
        // because the fragment doesnt have an activity we pull out the context using holder.mView.getCointext().
        // this is the adapter for the spinner list answers.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(holder.mView.getContext(), android.R.layout.simple_spinner_item, mValues.get(position).getAnswers());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        holder.answeresFB.setAdapter(dataAdapter);
        String questionID = mValues.get(position).getId();

        // because in anonynmous class we cant reach the questionID varaible so we use a class that implements the ItemSelectedListener
        holder.answeresFB.setOnItemSelectedListener(new ItemSelected(questionID));
    }

    class ItemSelected implements AdapterView.OnItemSelectedListener {
        private String questionID;

        public ItemSelected(String questionID) {
            this.questionID = questionID;
        }

        @Override
        //itemPosition - is the position of the selected answer in the spinner
        public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id) {
            answer.put(questionID, itemPosition);
            Log.d(TAG, questionID + "," + itemPosition);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void onSavePress() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance(); // creating database object
        DatabaseReference dbRootRef = mDatabase.getReference(); // creating reference to our database

        UserAnswerInformation userAnswerInformation = MatchingAlgorithmSingleton.getInstance().getMe();
        userAnswerInformation.setAnswer(answer);
        dbRootRef.child("UserAnswer").child(user.getUid()).setValue(userAnswerInformation);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DummyItem mItem;
        //whats inside the one item layout includes:
        public TextView questionFB;
        public Spinner answeresFB;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            questionFB = (TextView) view.findViewById(R.id.QuestionFB);
            answeresFB = (Spinner) view.findViewById(R.id.AnsweresFB);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + questionFB.getText() + "'";
        }
    }
}