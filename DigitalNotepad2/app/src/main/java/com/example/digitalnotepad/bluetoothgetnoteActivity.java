package com.example.digitalnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class bluetoothgetnoteActivity extends AppCompatActivity {

    public final int BT_REQUEST_ENABLE = 123;
    public final int BT_VISIBLE_ENABLE = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetoothgetnote);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter==null)
        {
            Toast.makeText(getBaseContext(), "Your phone does not support bluetooth.!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!btAdapter.isEnabled())
        {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, BT_REQUEST_ENABLE);
            return;
        }

        makeBTDiscoverable();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==BT_REQUEST_ENABLE)
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
        else if (requestCode==BT_VISIBLE_ENABLE)
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
        startActivityForResult(discoverableIntent, BT_VISIBLE_ENABLE);
    }
}
