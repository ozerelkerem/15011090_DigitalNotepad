package com.example.digitalnotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

public class ListRemindersActivity extends AppCompatActivity {

    private ImageView newreminderImageView;



    private RecyclerView reminderlistlistRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reminders);

        reminderlistlistRecyclerView = (RecyclerView)findViewById(R.id.reminderlistRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        reminderlistlistRecyclerView.setLayoutManager(layoutManager);

        newreminderImageView = (ImageView) findViewById(R.id.newreminderImageView);

        newreminderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRemindersActivity.this, editreminderActivity.class);
                intent.putExtra("reminder", new Reminder(new Date()));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

       updateList();
    }
    private void updateList()
    {
        Reminder[] mydata = App.getReminders();
        if  (mydata==null)
        {
            Toast.makeText(this, "No Reminder",Toast.LENGTH_LONG).show();
            reminderlistlistRecyclerView.setAdapter(null);
        }
        else
        {
            mAdapter = new ReminderListAdapter(mydata);
            reminderlistlistRecyclerView.setAdapter(mAdapter);
        }
    }
}
