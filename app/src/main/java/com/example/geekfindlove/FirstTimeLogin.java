package com.example.geekfindlove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
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
    private EditText age;
    private Spinner location;
    private RadioButton male;
    private RadioButton female;
    private RadioButton men;
    private RadioButton women;
    private RadioButton both;
    private Button save_data;
    private Button upload_picture;
    private EditText phone;
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
        female = (RadioButton) findViewById(R.id.femalRadio);
        male = (RadioButton) findViewById(R.id.maleRadio);
        both = (RadioButton) findViewById(R.id.radioButtonBoth);
        men = (RadioButton) findViewById(R.id.radioButtonMen);
        women = (RadioButton) findViewById(R.id.radioButtonWomen);
        save_data = (Button) findViewById(R.id.saveData);
        upload_picture = (Button) findViewById(R.id.buttonUpload);
        pic = (ImageView) findViewById(R.id.imageViewPic);
        age = (EditText) findViewById(R.id.editTextAge);
        location = (Spinner) findViewById(R.id.location);
        phone = (EditText) findViewById(R.id.phoneNumber);

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
        // if the singleTon was null, and the instance of getMe was null, it means the user is not
        // in the tree yet, and therefore we dont have any information to load to edit page.
        // we enter only if its not null-> only if theres information to load
        if( MatchingAlgorithmSingleton.getInstance().getMe().getUserDetails()!=null)
            setDetailsWhenEditProfile();
    }
    // this function is used for loading the user information into the edit profile page.
    // each user will decide which of the parameters he would like to change, and if he doesnt change
    // it remains the same as it was before.
    private void setDetailsWhenEditProfile(){
        UserInformation userDetails = MatchingAlgorithmSingleton.getInstance().getMe().getUserDetails();
        fn.setText(userDetails.getFn());
        ln.setText(userDetails.getLn());
        if (userDetails.getGender().equals("Female")) {
            female.setChecked(true);
        } else {
            male.setChecked(true);
        }
        if (userDetails.getActualOrientation().equals("Both")) {
            both.setChecked(true);
        } else if (userDetails.getActualOrientation().equals("Male"))
            men.setChecked(true);
        else {
            women.setChecked(true);
        }
       age.setText(String.valueOf(userDetails.getAge()));

        if (userDetails.getLocation().equals("Location"))
            location.setSelection(0);
        else if (userDetails.getLocation().equals("North"))
            location.setSelection(1);
        else if (userDetails.getLocation().equals("Center"))
            location.setSelection(2);
        else
            location.setSelection(3);

        phone.setText(userDetails.getPhone());

        // setting the picture
         FirebaseStorage mDatabase; // creating database object
         StorageReference dbRef; // creating reference to our database

        // thats the references for the pointer to Storage in firebase(for picture)
        mDatabase=FirebaseStorage.getInstance();
        dbRef = mDatabase.getReference().child("Uploads/"+userDetails.getId()+"/profile");

        try {
            File localFile = File.createTempFile("images", "jpg");
            dbRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    pic.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Operation made successfully", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        });
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "image didnt upload successfully", Toast.LENGTH_LONG).show();
            }


        }
    };

    public void saveRegisterButtonClick(View view) {
        int ageInt = Integer.parseInt(age.getText().toString());
        String locationStr = location.getSelectedItem().toString();
        String gender = female.isChecked() ? "Female" : "Male";
        String interestedIn = intrestedIn();
        user = new UserInformation(fn.getText().toString(), ln.getText().toString(), email.getText().toString(), gender, "0", ageInt, locationStr, interestedIn, phone.getText().toString());
        user.setId(userId); // user id is the key.
        // inserting into user node a child - new node as the new user.id that we recieved.
        // note : if user isnt made it will create it and then insert the new node
        dbRootRef.child("users").child(user.getId()).setValue(user, completionListener); // the completionListener can tell us if the save succeeded or not.
        // once a user is being register, we want to save his information also in the UserAnsweres in firebase, so there we wont need to search him up all over again‏
        dbRootRef.child("UserAnswer").child(user.getId()).child("userDetails").setValue(user);
    }

    private String intrestedIn() {

        if (men.isChecked()) {
            return "Male";
        } else if (women.isChecked()) {
            return "Female";
        } else {
            return "Both";
        }
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