package com.example.digitalnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class editnoteActivity extends AppCompatActivity {

    public static final int BT_REQUEST_ENABLE = 123;
    private Note note;
    private Toolbar toolbar;

    private EditText titleEditText, descEditText;
    private Spinner colorSpinner, prioritySpinner;
    private ImageView saveImageView,deleteImageView,attachmentsImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Note Details");

        note = (new Gson()).fromJson(getIntent().getExtras().get("note").toString(),Note.class);
        note.Save();

        titleEditText = (EditText)findViewById(R.id.titleEditText);
        descEditText = (EditText)findViewById(R.id.descEditText);
        colorSpinner = (Spinner)findViewById(R.id.colorSpinner);
        prioritySpinner = (Spinner)findViewById(R.id.prioritySpinner);
        saveImageView = (ImageView)findViewById(R.id.saveImageView);
        deleteImageView = (ImageView)findViewById(R.id.deleteImageView);
        attachmentsImageView = (ImageView)findViewById(R.id.attachmentsImageView);

        titleEditText.setText(note.getTitle());
        descEditText.setText(note.getDescription());

        colorSpinner.setSelection(Arrays.asList(NoteColor.values()).indexOf(note.getColor()));
        prioritySpinner.setSelection(Arrays.asList(NotePriority.values()).indexOf(note.getPriority()));

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorSpinner.setBackgroundColor(NoteColor.values()[position].c);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setTitle(titleEditText.getText().toString());
                note.setDescription(descEditText.getText().toString());
                note.setColor(NoteColor.values()[colorSpinner.getSelectedItemPosition()]);
                note.setPriority(NotePriority.values()[prioritySpinner.getSelectedItemPosition()]);
                note.Save();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                editnoteActivity.this.onBackPressed();
            }
        });
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.Delete();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                editnoteActivity.this.onBackPressed();
            }
        });

        attachmentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editnoteActivity.this, noteattachmentsActivity.class);
                intent.putExtra("note", (new Gson()).toJson(note));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==BT_REQUEST_ENABLE)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(getBaseContext(), "Bluetooth activated. Try to send now.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "You have to enable your bluetooth.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_menu, menu);

        MenuItem mi =  menu.findItem(R.id.send_note);
        mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if(btAdapter==null)
                {
                    Toast.makeText(getBaseContext(), "Your phone does not support bluetooth.!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!btAdapter.isEnabled())
                {
                    Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBTIntent, BT_REQUEST_ENABLE);
                    return false;
                }
                Intent intent = new Intent(getApplicationContext(), bluetoothListActivity.class);
                intent.putExtra("note",  (new Gson()).toJson(note));
                startActivity(intent);
                return false;
            }
        });

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        note = App.getNote(note.id);
    }

}
