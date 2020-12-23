package com.example.geekfindlove;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminNewQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class AdminNewQuestionFragment extends Fragment implements View.OnClickListener, DatabaseReference.CompletionListener {

    private static final String TAG = "AdminNewQuestion";
    private Button buttonAnswer;
    private Button buttonDelAnswer;
    private Button buttonSend;
    private EditText editTextAnswerText;
    private EditText editTextQuestionText;
    private ArrayList<String> listAnswer;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> itemToDelete;
    private int toDelete = -1;
    ListView listView;

    public AdminNewQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminNewQuestion.
     */
    public static AdminNewQuestionFragment newInstance() {
        AdminNewQuestionFragment fragment = new AdminNewQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_new_question, container, false);
        buttonAnswer = (Button) view.findViewById(R.id.buttonAddAnswer);
        editTextAnswerText = (EditText) view.findViewById(R.id.answerText);
        editTextQuestionText = (EditText) view.findViewById(R.id.question_text);
        listView = (ListView) view.findViewById(R.id.listView);
        buttonSend = (Button) view.findViewById(R.id.buttonSend);
        buttonDelAnswer = (Button) view.findViewById(R.id.buttonDel);

        buttonAnswer.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
        buttonDelAnswer.setOnClickListener(this);

        itemToDelete = new ArrayList<>();
        listAnswer = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listAnswer);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemSelected());

        listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {

            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                view1.setEnabled(true);
            }
        });


        return view;
    }

    class OnItemSelected implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            if (view.isEnabled()) {
                view.setEnabled(false);
                itemToDelete.add(i);
                //toDelete = i;
            } else {
                view.setEnabled(true);
                itemToDelete.remove(i);
                // toDelete = -1;


            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAnswer) {

            CharSequence answer = editTextAnswerText.getText();
            if (answer.length() == 0)
                return;

            listAnswer.add(answer.toString());
            adapter.notifyDataSetChanged();
            editTextAnswerText.setText("");


        } else if (v == buttonSend) {
            String question = editTextQuestionText.getText().toString();
            QuestionsInformation questionsInformation = new QuestionsInformation(question, listAnswer);

            DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference("questions");
            String id = dbRootRef.push().getKey();
            dbRootRef.child(id).setValue(questionsInformation, this);

        } else if (v == buttonDelAnswer) {

            for (int pos : itemToDelete) {
                //if (toDelete != -1) {

                //listView.removeViews(toDelete, 1);
                listView.removeViews(pos, 1);
                listAnswer.remove(pos);
                //itemToDelete.remove(pos);
                adapter.notifyDataSetChanged();
                // toDelete = -1;


                //  }
            }
            itemToDelete.clear();

        }

    }

    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference
            databaseReference) {
        if (error != null) // if != null it means we did recevie a msg, so it means we got an error
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getContext(), R.string.upload_success, Toast.LENGTH_LONG).show();
            adapter.clear();
            listAnswer.clear();
            editTextQuestionText.setText("");
            editTextAnswerText.setText("");
        }
    }
}