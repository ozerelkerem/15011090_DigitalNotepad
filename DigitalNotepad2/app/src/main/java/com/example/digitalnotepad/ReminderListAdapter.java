package com.example.digitalnotepad;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;


public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.MyViewHolder2> {
    private Reminder[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public Reminder reminder;
        public ConstraintLayout constraintLayout;
        public TextView timeTextView,dateTextView,remindertypeTextView;
        public MyViewHolder2(ConstraintLayout v) {
            super(v);
            constraintLayout = v;

            timeTextView = (TextView)v.getViewById(R.id.remindertimeTextView);
            dateTextView = (TextView)v.getViewById(R.id.reminderdateTextView);
            remindertypeTextView=(TextView)v.getViewById(R.id.remindertypeTextView);
         //   createdagoTextView = (TextView)v.getViewById(R.id.createdagoTextView);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(App.getContext(), editreminderActivity.class);
                    intent.putExtra("reminder", reminder);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReminderListAdapter(Reminder[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReminderListAdapter.MyViewHolder2 onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_recyclerview, parent, false);

        MyViewHolder2 vh = new MyViewHolder2(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder2 holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Reminder n = mDataset[position];
        holder.reminder = n;
        holder.dateTextView.setText(n.getDateS());
        holder.timeTextView.setText(n.getTimeS());
        holder.remindertypeTextView.setText(
                App.getContext().getResources().getStringArray(R.array.remindertypes)[Arrays.asList(ReminderType.values()).indexOf(n.getType())]
        );;



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
