package com.example.digitalnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ListNotesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView newnoteImageView;

    private RecyclerView notelistRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Note List");


        notelistRecyclerView = (RecyclerView) findViewById(R.id.notelistRecyclerView);
        newnoteImageView = (ImageView) findViewById(R.id.newnoteImageView);

        notelistRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        notelistRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)


        newnoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNotesActivity.this, editnoteActivity.class);
                Note note = new Note();

                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                String provider = lm.getBestProvider(new Criteria(), true);
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"First give permissions for location", Toast.LENGTH_LONG).show();
                    return;
                }
                Location l = lm.getLastKnownLocation(provider);
                if(l!=null)
                    note.setLocation(new LatLng(l.getLatitude(), l.getLongitude()));
            intent.putExtra("note",  (new Gson()).toJson(note));
            startActivity(intent);
        }
    });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem search =  menu.findItem(R.id.app_bar_search);
        ((SearchView)search.getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Note[] list = App.getNotes();
                if(list!=null){

                    list =  Arrays.stream(list).filter(new Predicate<Note>() {
                        @Override
                        public boolean test(Note note) {
                            return note.getTitle().contains(newText);
                        }

                    }).collect(Collectors.toList()).toArray(new Note[0]);
                    if(list!=null)
                        updaterView(list);
                }


                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("Desc Created Date"))
        {
            Note[] list = App.getNotes();
            if(list!=null)
            {
                Arrays.sort(list, new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getCreatedDate().compareTo(o2.getCreatedDate());
                    }
                });
                updaterView(list);
            }



        }
        else if (item.getTitle().toString().equals("Asc Created Date"))
        {
            Note[] list = App.getNotes();
            if(list!=null)
            {
                Arrays.sort(list, new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                    }
                });
                updaterView(list);
            }

        }

        else if(item.getTitle().toString().equals("Get Note"))
        {
            Intent intent = new Intent(this, bluetoothgetnoteActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    private void updaterView(Note[] data)
    {
        mAdapter = new NoteListAdapter(data);
        notelistRecyclerView.setAdapter(mAdapter);
    }
    private void updateList()
    {
        Note[] mydata = App.getNotes();
        if  (mydata==null)
        {
            Toast.makeText(this, "No Note",Toast.LENGTH_LONG).show();
            notelistRecyclerView.setAdapter(null);
        }
        else
        {
            updaterView(mydata);
        }
    }
}
