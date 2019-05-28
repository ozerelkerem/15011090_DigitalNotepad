package com.example.digitalnotepad;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;


public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private Note[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public Note note;
        public ConstraintLayout constraintLayout;
        public TextView titleTextView,descTextView,createdagoTextView,priTextView;
        public MyViewHolder(ConstraintLayout v) {
            super(v);
            constraintLayout = v;

            titleTextView = (TextView)v.getViewById(R.id.titleTextView);
            descTextView = (TextView)v.getViewById(R.id.descTextView);
            createdagoTextView = (TextView)v.getViewById(R.id.createdagoTextView);
            priTextView = (TextView)v.getViewById(R.id.priorityTextView);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(App.getContext(), editnoteActivity.class);
                    intent.putExtra("note",  new Gson().toJson(note));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoteListAdapter(Note[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_recyclerview, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Note n = mDataset[position];
        holder.note = n;
        holder.titleTextView.setText(n.getTitle());
        holder.descTextView.setText(n.getDescription());
        holder.createdagoTextView.setText(n.getCreatedAgoTime());
        holder.constraintLayout.setBackgroundColor(n.getColor().c);
        holder.priTextView.setText(n.getPriority().toString());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
