package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    // firebase
    private boolean answer; // variable in order to identify if a user finished answering his information settings
    private FirebaseDatabase mDatabase; // creating database object
    private DatabaseReference dbRef; // creating reference to our database

    //Buttons
    private Button signIn;
    private Button signUpOrSwitchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //user can go to this activity by press back button,
        signIn = (Button) findViewById(R.id.sign_in);
        signUpOrSwitchUser = (Button) findViewById(R.id.sign_up_or_switch_user);

        signIn.setOnClickListener(this);
        signUpOrSwitchUser.setOnClickListener(this);

        // firebase
        mDatabase = FirebaseDatabase.getInstance();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {//not sign
            openSignInActivity();
        } else { //already sign
            if (MatchingAlgorithmSingleton.getInstance().getMe().getUserDetails() != null) {
                MatchingAlgorithmSingleton.logout_setNULL();
            } else {
                signIn(user);
            }
        }
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
                signIn(user);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openSignInActivity() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        //sent to build-in activity for authentication firebase.
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void signIn(FirebaseUser user) {
        String s = "sign in is " + user.getDisplayName();
        signIn.setText(s);
        dbRef = mDatabase.getReference("/users/" + user.getUid());
        dbRef.addValueEventListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dbRef != null)
            dbRef.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
        /*
          if a user logged in into the app for the first time -> he goes to information page.
            if a user logged in for the first time and closed the app before finishing setting up the information, it means that the second time he will be
            redirected to home page rather than information page, and therefore he didnt complete answering the setting details we need.
            now we need to check. **if user == null is not finishing setting up the information**.
         */
        if (userInformation == null) {//user not in the first time.
            Intent intent = new Intent(this, FirstTimeLogin.class);
            startActivity(intent);
        } else if (userInformation.isAdmin()) {
            Intent intent = new Intent(this, AdminActivity1.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, UserActivity1.class);
            startActivity(intent);
        }


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(this, "oops something's wrong. place try enter to app later.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v == signIn) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null)
                signIn(user);
            else
                Toast.makeText(this, "oops something's wrong. place try switch user and enter the main and password again.", Toast.LENGTH_LONG).show();
        } else if (v == signUpOrSwitchUser)
            openSignInActivity();
    }
}