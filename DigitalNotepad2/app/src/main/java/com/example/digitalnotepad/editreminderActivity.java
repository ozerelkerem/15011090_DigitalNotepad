package com.example.digitalnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;
import java.util.Calendar;

public class editreminderActivity extends AppCompatActivity {

    private Reminder reminder;

    private MapView mapView;
    private GoogleMap gmap;

    private Button setlocationButton;
    private SeekBar radiusSeekBar;
    private TextView dateTextView, timeTextView;
    private ImageView saveImageView, deleteImageView;
    private Spinner remindertypeSpinner, notelistSpinner;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;
    private FusedLocationProviderClient fusedLocationClient;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editreminder);

        notelistSpinner = (Spinner) findViewById(R.id.notelistSpiiner);
        remindertypeSpinner = (Spinner) findViewById(R.id.remindertypeSpinner);
        saveImageView = (ImageView)findViewById(R.id.saveImageView);
        deleteImageView = (ImageView)findViewById(R.id.deleteImageView);
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        timeTextView = (TextView)findViewById(R.id.timeTextView);
        setlocationButton = (Button) findViewById(R.id.setlocationButton);
        radiusSeekBar = (SeekBar) findViewById(R.id.radiusSeekBar);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(new Bundle());

        reminder = (Reminder) getIntent().getExtras().get("reminder");
        notelistSpinner.setSelection(Arrays.asList(App.getNotes()).indexOf(reminder.getNote()));
        setTextViews();

        remindertypeSpinner.setSelection(Arrays.asList(ReminderType.values()).indexOf(reminder.getType()));

        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
                android.R.layout.simple_spinner_item, App.getNotes());

        notelistSpinner.setAdapter(adapter);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        gmap = googleMap;
                        gmap.setMinZoomPreference(12);
                        LatLng ny = new LatLng(location.getLatitude(), location.getLongitude());
                        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));

                        gmap.setMyLocationEnabled(true);
                        gmap.getUiSettings().setMyLocationButtonEnabled(true);
                        gmap.getUiSettings().setIndoorLevelPickerEnabled(true);
                        drawTarget();
                    }

                });






            }
        });
        final ImageView dropPinView = new ImageView(getApplicationContext());
        dropPinView.setImageResource(R.drawable.pin);

// Statically Set drop pin in center of screen
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        float density = getResources().getDisplayMetrics().density;
        params.bottomMargin = (int) (12 * density);
        dropPinView.setLayoutParams(params);
        mapView.addView(dropPinView);

        setlocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng position = gmap.getProjection().fromScreenLocation(new Point(dropPinView.getLeft() + (dropPinView.getWidth() / 2), dropPinView.getBottom()));
                reminder.setLocation(position);
                reminder.setLocradius(radiusSeekBar.getProgress());
                drawTarget();
            }
        });

       dateTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(reminder.getDate());
               DatePickerDialog dpd = new DatePickerDialog(editreminderActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       Calendar clndr = Calendar.getInstance();
                       clndr.setTime(reminder.getDate());
                       clndr.set(Calendar.YEAR, year);
                       clndr.set(Calendar.MONTH, month);
                       clndr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                       reminder.setDate(clndr.getTime());
                       setTextViews();
                   }
               },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

               dpd.show();
           }
       });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(reminder.getDate());
                TimePickerDialog tpd = new TimePickerDialog(editreminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar clndr = Calendar.getInstance();
                        clndr.setTime(reminder.getDate());
                        clndr.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        clndr.set(Calendar.MINUTE, minute);
                        reminder.setDate(clndr.getTime());
                        setTextViews();
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                tpd.show();
            }
        });


        saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reminder.setType(ReminderType.values()[remindertypeSpinner.getSelectedItemPosition()]);
                reminder.Save();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                editreminderActivity.this.onBackPressed();
            }
        });
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminder.Delete();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                editreminderActivity.this.onBackPressed();
            }
        });



    }

    private void setTextViews()
    {
        dateTextView.setText(reminder.getDateS());
        timeTextView.setText(reminder.getTimeS());
        radiusSeekBar.setProgress((int)reminder.getLocradius());
    }
    private void drawTarget()
    {
        gmap.clear();
        for (Note note : App.getNotes())
        {
            if(note.getLocation().longitude != 0)
            {
                MarkerOptions mo = new MarkerOptions();
                mo.position(note.getLocation());
                mo.title(note.getTitle());
                gmap.addMarker(mo);
                int c = NoteColor.values()[Arrays.asList(NoteColor.values()).indexOf(note.getColor())].c;
                gmap.addCircle(new CircleOptions().center(note.getLocation()).radius(500f).fillColor(c).strokeColor(c));
            }
        }

        if (reminder.getLocation()==null)
            return;



        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(reminder.getLocation());
        gmap.addMarker(markerOptions);


        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(reminder.getLocation());
        circleOptions.radius(reminder.getLocradius());
        gmap.addCircle(circleOptions);



    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                else {

                }
            }
        }
      //  updateLocationUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
