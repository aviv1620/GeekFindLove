package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class FirstTimeLogin extends AppCompatActivity {
    private static final String TAG = "FirstTimeLogin";

    private UserInformation user;
    private EditText fn; // first name
    private EditText ln; // last name
    private EditText email; // first name
    private RadioButton Male;
    private RadioButton Female;
    private Button save_data;
    private Button upload_picture;
    private Button capture_pciture;
    // to upload picture
    private Uri imageUri;
    private ImageView pic;


    //user information
    private String userId;

    // firebase
    private FirebaseDatabase mDatabase; // creating database object
    private DatabaseReference dbRootRef; // creating reference to our database

    private static final int IMAGE_REQUEST = 2;
    private static final int CAMERA_REQUEST_CODE = 0;


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
        save_data = (Button) findViewById(R.id.saveData);
        upload_picture = (Button) findViewById(R.id.buttonUpload);
        capture_pciture = (Button) findViewById(R.id.buttonCapture);
        pic = (ImageView) findViewById(R.id.imageViewPic);
        mDatabase = FirebaseDatabase.getInstance();
        dbRootRef = mDatabase.getReference();

        //get user information
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e(TAG, "user not log in");
        }

        // for representing the email of the current user.
        userId = user.getUid();
        String emailStr = user.getEmail();
        email.setText(emailStr);
    }

    // defining completionListener -> it will tell us if the saving has been succeeded or not.
    //it is linked to our database
    DatabaseReference.CompletionListener completionListener = (error, ref) -> {
        if (error != null) // if != null it means we did recevie a msg, so it means we got an error
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        else { // else it means we succeeded in saving the new client
            Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, UserActivity1.class);
            startActivity(intent);
        }
    };

    public void saveRegisterButtonClick(View view) {
        user = new UserInformation(fn.getText().toString(), ln.getText().toString(), email.getText().toString(), Female.isChecked() ? "Female" : "Male", "0");
        user.setId(userId); // user id is the key.
        // inserting into user node a child - new node as the new user.id that we recieved.
        // note : if user isnt made it will create it and then insert the new node
        dbRootRef.child("users").child(user.getId()).setValue(user, completionListener); // the completionListener can tell us if the save succeeded or not.

    }


    public void OnUploadOrCaptureClick(View view) {

        openImage(view);

    }

    private void openImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/");

        // if the user wants to uplaod a picture from its gallery.
        if (view == findViewById(R.id.buttonUpload)) {
            Log.v("Elad", "okkkkkkkkk111111");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);

        }
        // if the user wants to take a picture using camera.
        else if (view == findViewById(R.id.buttonCapture)) {
            Log.v("Elad", "okkkkkkkkk22222222");
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           // intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent1, CAMERA_REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (( requestCode == IMAGE_REQUEST ||requestCode == CAMERA_REQUEST_CODE) && resultCode == RESULT_OK) { // getting the requested code
            imageUri = data.getData();
            if(imageUri == null){
                Log.v("Elad","NULL");
            }
            //upLoadImage();

        }
    }

    public void upLoadImage() {
        // using progress dialog while the image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();



        if (imageUri != null) {
            Log.v("Elad","its done");
            // creating pointer to the Storage reference in FireBase
            //final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Uploads").child(userId).child(System.currentTimeMillis() + " " + getFileExtention(imageUri));
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Uploads").child(userId).child(imageUri.getLastPathSegment());
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.v("Elad", url);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "image upload successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "image didnt upload successfully", Toast.LENGTH_LONG).show();
        }


    }

    private String getFileExtention(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }
}