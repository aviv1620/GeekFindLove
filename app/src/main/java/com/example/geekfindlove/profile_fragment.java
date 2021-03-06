package com.example.geekfindlove;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_fragment extends androidx.fragment.app.Fragment implements ValueEventListener, View.OnClickListener { // we use extends androidx.fragment.app.Fragment instead od extend Fragment
//
    private  TextView hello;
    private ImageView profilePic;
    private FirebaseStorage mDatabase; // creating database object
    private StorageReference dbRef; // creating reference to our database
    private FirebaseDatabase mDatabase2; // creating database object
    private DatabaseReference dbRef2; // creating reference to our database

    private Button editButton;
    private Button logout;
    //    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public profile_fragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment profile_fragment.
//     */
//    // TODO: Rename and change types and number of parameters
    public static profile_fragment newInstance() {

        profile_fragment fragment = new profile_fragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_profile_fragment, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        // thats the references for the pointer to Storage in firebase(for picture)
        mDatabase=FirebaseStorage.getInstance();
        mDatabase2 =FirebaseDatabase.getInstance();
        // thats the references for the pointer to Real Time Data Base  in firebase(for user information);
        dbRef = mDatabase.getReference().child("Uploads/"+user.getUid()+"/profile");
        dbRef2=mDatabase2.getReference().child("/users/" + user.getUid());
        dbRef2.addValueEventListener(this);




        profilePic = (ImageView) view.findViewById(R.id.imageViewProfile);
        hello = (TextView) view.findViewById(R.id.textViewHello);
        editButton = (Button)view.findViewById(R.id.buttonEditProfile);
        logout= (Button)view.findViewById(R.id.logout);

        editButton.setOnClickListener(this);
        logout.setOnClickListener(this);


        try {
            File localFile = File.createTempFile("images", "jpg");
            dbRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profilePic.setImageBitmap(bitmap);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
        hello.setText("Hello " + userInformation.getFn() + " "+ userInformation.getLn());

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onClick(View v) {
        if(v==editButton) {
            Intent i = new Intent(getActivity(), FirstTimeLogin.class);
            startActivity(i);
        }
        else if(v==logout){
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        }

    }
}