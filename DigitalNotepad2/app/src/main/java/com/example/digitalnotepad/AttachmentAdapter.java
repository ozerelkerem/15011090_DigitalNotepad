package com.example.digitalnotepad;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;


public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.MyViewHolder2> {
    private ArrayList<Attachment> mDataset;
    private Note note;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public  Note note;
        public Attachment attachment;
        public ConstraintLayout constraintLayout;
        public ImageView fileImageView;

        public MyViewHolder2(ConstraintLayout v) {
            super(v);
            constraintLayout = v;

            fileImageView = (ImageView)v.findViewById(R.id.fileImageView);

            v.findViewById(R.id.deleteatImageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    note.getAttachments().remove(attachment);
                    note.Save();
                    noteattachmentsActivity.instance.updateList();
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AttachmentAdapter(Note myDataset) {
        note = myDataset;
        mDataset =  myDataset.getAttachments();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AttachmentAdapter.MyViewHolder2 onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_recyclerview, parent, false);

        MyViewHolder2 vh = new MyViewHolder2(v);
        vh.note = note;
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder2 holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Attachment n = mDataset.get(position);
        holder.attachment = n;

        if(n.getUri().contains("image")){



            Picasso.Builder builder = new Picasso.Builder(App.getContext());
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(n.getUri()).into(holder.fileImageView);


        }
        else
            holder.fileImageView.setImageResource(R.drawable.attachment);




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
