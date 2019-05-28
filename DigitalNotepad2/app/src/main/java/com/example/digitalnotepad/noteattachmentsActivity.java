package com.example.digitalnotepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Arrays;

public class noteattachmentsActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 123;
    public static noteattachmentsActivity instance;
    private Note note;

    private ImageView addatImageView;

    private RecyclerView attachmentRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteattachments);

        getPermissions();



        addatImageView = (ImageView)findViewById(R.id.addatImageView);

        attachmentRecyclerView = (RecyclerView)findViewById(R.id.attachmentsRecyclerView);

        note = (new Gson()).fromJson(getIntent().getExtras().get("note").toString(),Note.class);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        attachmentRecyclerView.setLayoutManager(layoutManager);

        addatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                startActivityForResult(intent, READ_REQUEST_CODE);





            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==111 && permissions.length>0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
        }
    }

    public void getPermissions()
    {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==READ_REQUEST_CODE && resultCode==RESULT_OK)
        {
            note.getAttachments().add(new Attachment(data.getData().toString()));
            note.Save();
            updateList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    public void updateList()
    {

        if  (note.getAttachments()==null)
        {
            Toast.makeText(this, "No Attachment",Toast.LENGTH_LONG).show();
            attachmentRecyclerView.setAdapter(null);
        }
        else
        {
            mAdapter = new AttachmentAdapter(note);
            attachmentRecyclerView.setAdapter(mAdapter);
        }
    }
}

