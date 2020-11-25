package com.example.geekfindlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {//not sign

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            //sent to build-in activity for authentication firebase.
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }else{//already sign
            signIn(user);
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
                Toast.makeText(this,"Sign in failed",Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    private  void signIn(FirebaseUser user){
        //TODO test if user is admin.
        //if sign move to profile.
        Intent intent = new Intent(this,UserActivity.class);
        startActivity(intent);
    }





}