package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//Todo: need to add Permissions for camera while the user needs to use it to insert a photo to his/hers profile.
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
    // to upload picture
    private Uri imageUri;
    private ImageView pic;
    private String userChoosenTask;
    private ProgressDialog progressDialog;



    //user information
    private String userId;

    // firebase
    private FirebaseDatabase mDatabase; // creating database object
    private DatabaseReference dbRootRef; // creating reference to our database

    private static final int IMAGE_REQUEST = 1;
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
        pic = (ImageView) findViewById(R.id.imageViewPic);
        mDatabase = FirebaseDatabase.getInstance();
        dbRootRef = mDatabase.getReference();

        pic.setImageResource(R.drawable.ic_launcher_background);



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
        else { // else it means we succeeded in saving the new client, now lets save picture.
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            if (imageUri != null) {
                Intent intent = new Intent(this, UserActivity1.class);
                final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Uploads").child(userId).child("profile");
                //final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Uploads").child(userId).child(imageUri.getLastPathSegment() + " " + getFileExtention(imageUri));
                fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                Log.v("Elad", url);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Registeration made successfully", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        });
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "image didnt upload successfully", Toast.LENGTH_LONG).show();
            }


                Log.v("Elad","OKKK");



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
        final CharSequence[] items = {"Take Photo", "Choose from gallery", "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstTimeLogin.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // boolean result = Utility

                if (items[i].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[i].equals("Choose from gallery")) {
                    userChoosenTask = "Choose from gallery";
                    galleryIntent();
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), IMAGE_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            imageUri = data.getData();

            if (requestCode == IMAGE_REQUEST) {
                onSelectFromHalleryResult(data);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                onCaptureImageResult(data);

            }



        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


        File des = new File(getFilesDir(), System.currentTimeMillis() + "jpg");

        FileOutputStream fo = null;

        try {
            des.createNewFile();
            fo = new FileOutputStream(des);
            fo.write(bytes.toByteArray());
            fo.close();
            imageUri = Uri.fromFile(des);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pic.setImageBitmap(bitmap);

    }

    private void onSelectFromHalleryResult(Intent data) {

        Bitmap bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            pic.setImageBitmap(bitmap);
        }
    }

    private String getFileExtention(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }
}