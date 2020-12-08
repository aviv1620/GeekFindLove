package com.example.geekfindlove;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class MatchingRecyclerViewAdapter extends RecyclerView.Adapter<MatchingRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MatchingRecycler";
    private List<MatchingInformation> mValues;

    public MatchingRecyclerViewAdapter(List<MatchingInformation> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_matching_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MatchingInformation mi =  mValues.get(position);
        UserInformation u = mi.getDstDetail();


        holder.mItem = mi;
        holder.MatchingName.setText(u.getFn() + " " + u.getLn());
        String p = mi.getPercent() + "%";
        holder.MatchingPercent.setText(p);
        //holder.showNumber TODO show the number.
        //holder.avatar TODO load the avatar.

        FirebaseStorage mDatabase = FirebaseStorage.getInstance();; // creating database object
        StorageReference dbRef; // creating reference to our database

        String path = "Uploads/"+mi.getUserIdDst()+"/profile";
        Log.d(TAG,path);
        dbRef = mDatabase.getReference().child(path);

        try {
            File localFile = File.createTempFile("images", "jpg");
            holder.absolutePath = localFile.getAbsolutePath();
            dbRef.getFile(localFile).addOnSuccessListener(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setmValues(ArrayList<MatchingInformation> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged(); // notifying android that we changed the list,refresh the list that was empty at first.
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnSuccessListener<FileDownloadTask.TaskSnapshot> {
        public final TextView MatchingName;
        public final TextView MatchingPercent;
        public final Button showNumber;
        public final ImageView avatar;
        public MatchingInformation mItem;

        public String absolutePath;

        public ViewHolder(View view) {
            super(view);

            MatchingName = (TextView) view.findViewById(R.id.textViewMatchingName);
            MatchingPercent = (TextView) view.findViewById(R.id.textViewMatchingPercent);
            showNumber = (Button)view.findViewById(R.id.buttonMatchingshowNumber);
            avatar =  (ImageView)view.findViewById(R.id.imageViewMatchingAvatar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + MatchingName.getText() + "'";
        }

        @Override
        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
            avatar.setImageBitmap(bitmap);
        }
    }
}