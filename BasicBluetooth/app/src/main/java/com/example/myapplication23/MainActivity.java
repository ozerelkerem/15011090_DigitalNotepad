package com.example.myapplication23;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button acbtn,gndrbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acbtn = findViewById(R.id.button);
        gndrbtn = findViewById(R.id.button2);

        gndrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if(btAdapter==null)
                {
                    Toast.makeText(getBaseContext(), "Your phone does not support bluetooth.!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!btAdapter.isEnabled())
                {
                    Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBTIntent, 123);
                    return;
                }
              startActivity(new Intent(getApplicationContext(), bluetoothListActivity.class));


            }
        });

        acbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if(btAdapter==null)
                {
                    Toast.makeText(getBaseContext(), "Your phone does not support bluetooth.!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!btAdapter.isEnabled())
                {
                    Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBTIntent, 123);
                    return;
                }

                makeBTDiscoverable();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123)
        {
            if(resultCode == RESULT_OK)
            {
                makeBTDiscoverable();
                Toast.makeText(getBaseContext(), "Bluetooth activated. Try to send now.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "You have to enable your bluetooth.", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode==12)
        {
            if(resultCode == 300) //DISCOVERABILITY_DURATION
            {
                Toast.makeText(getBaseContext(), "Bluetooth View activated. Try to send now.", Toast.LENGTH_LONG).show();
                creteServer();
            }
            else
            {
                Toast.makeText(getBaseContext(), "You have to enable your bluetooth view.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void creteServer()
    {

        Thread serverThread = new ServerBTConnection(BluetoothAdapter.getDefaultAdapter());
        serverThread.run();
    }
    private void makeBTDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, 12);
    }
}
