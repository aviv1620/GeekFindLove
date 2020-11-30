package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    // firebase
    private boolean answer; // variable in order to identify if a user finished answering his information settings
    private FirebaseDatabase mDatabase; // creating database object
    private  DatabaseReference dbRootRef; // creating reference to our database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase=FirebaseDatabase.getInstance();
        dbRootRef=mDatabase.getReference();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {//not sign

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            //sent to build-in activity for authentication firebase.
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);

        } else { //already sign
            /*
            if a user logged in into the app for the first time -> he goes to information page.
            if a user logged in for the first time and closed the app before finishing setting up the information, it means that the second time he will be
            redirected to home page rather than information page, and therefore he didnt complete answering the setting details we need.
            now we need to check by his email if he complited answering, if the flag is true-> means he coplited
            if the flag is false -> redirected to information page
             */
              if(identifying_user_by_email(user.getEmail()))
                    signIn(user);
              else{
                  Intent intent = new Intent(this, FirstTimeLogin.class);
                  startActivity(intent);
              }

        }
    }

    private boolean identifying_user_by_email(String email) {
        answer=false;
        DatabaseReference dbRef;
        dbRef= mDatabase.getReference("/user");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){ // going through all of the users
                     UserInformation user = child.getValue(UserInformation.class);
                     if(user.getEmail()==email) {
                         answer = user.isFinished_registration();
                         break;
                     }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return answer;
    }

    @Override
    //wait for result from build in ui.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // signIn(user);
                // since its the first time that the user log in -> we direct them to the information page, to fill up user settings
                Intent intent = new Intent(this, FirstTimeLogin.class);
                startActivity(intent);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    private void signIn(FirebaseUser user) {
        //TODO test if user is admin.
        //if sign move to profile.
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


}