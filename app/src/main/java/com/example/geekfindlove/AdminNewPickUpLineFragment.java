package com.example.geekfindlove;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminNewPickUpLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminNewPickUpLineFragment extends Fragment implements View.OnClickListener, DatabaseReference.CompletionListener {
    private EditText editTextTextMultiLine;

    public AdminNewPickUpLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminNewPickUpLineFragment.
     */
    public static AdminNewPickUpLineFragment newInstance() {
        AdminNewPickUpLineFragment fragment = new AdminNewPickUpLineFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_new_pick_up_line, container, false);
        editTextTextMultiLine = (EditText)view.findViewById(R.id.editTextTextMultiLine);
        Button sendButton = (Button)view.findViewById(R.id.button2);
        sendButton.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        String pickUpLine = editTextTextMultiLine.getText().toString();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = mDatabase.getReference("pickUpLine");
        String key = dbRef.push().getKey();
        dbRef.child(key).setValue(pickUpLine, this);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference databaseReference) {
        if (error != null) // if != null it means we did recevie a msg, so it means we got an error
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getContext(), R.string.send_success, Toast.LENGTH_LONG).show();
            editTextTextMultiLine.setText("");
        }
    }
}