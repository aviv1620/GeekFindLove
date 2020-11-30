package com.example.geekfindlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstTimeLogin extends AppCompatActivity {
    private  UserInformation user;
    private  EditText fn; // first name
    private  EditText ln; // last name
    private  EditText email; // first name
    private RadioButton Male;
    private RadioButton Female;
    private Button save_data;

    // firebase
    private FirebaseDatabase mDatabase; // creating database object
    private DatabaseReference dbRootRef; // creating reference to our database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login);

        //buttons and edit-text
        fn = (EditText) findViewById(R.id.firstName);
        ln = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        Female = (RadioButton) findViewById(R.id.maleRadio);
        Male = (RadioButton) findViewById(R.id.femalRadio);
        save_data=(Button) findViewById(R.id.saveData);


        mDatabase=FirebaseDatabase.getInstance();
        dbRootRef=mDatabase.getReference();
    }

    // defining completionListener -> it will tell us if the saving has been succeeded or not.
    //it is linked to our database
    DatabaseReference.CompletionListener completionListener= (error, ref) -> {
        if (error != null) // if != null it means we did recevie a msg, so it means we got an error
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        else{ // else it means we succeeded in saving the new client
            Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_LONG).show();
            user.setFinished_registration(true);
            /*
            TO DO!!!!!!!!!!!!!!!!
            here we need to update the user boolean variable in the firebase to be true, and not
            only the object that in here.
             */
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
    }
    };

    public void saveRegisterButtonClick(View view){
        user= new UserInformation(fn.getText().toString(),ln.getText().toString(),email.getText().toString(),Female.isChecked()?"Female" : "Male", "0");
        user.setId(dbRootRef.push().getKey()); // we create a unique key in that way
        // inserting into user node a child - new node as the new user.id that we recieved.
         // note : if user isnt made it will create it and then insert the new node
        dbRootRef.child("users").child(user.getId()).setValue(user,completionListener); // the completionListener can tell us if the save succeeded or not.

    }
}