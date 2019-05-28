package com.example.digitalnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class bluetoothListActivity extends AppCompatActivity {

    private ListView bluetoothListView;
    private ArrayList<String> btDeviceList;
    private ArrayList<BluetoothDevice> btDeviceLis;
    private ArrayAdapter btArrayAdapter;

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        btDeviceLis = new ArrayList<>();
        btDeviceList = new ArrayList<String>();
        btArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, btDeviceList);
        bluetoothListView = (ListView) findViewById(R.id.bluetoothListView);

        note = (new Gson()).fromJson(getIntent().getExtras().get("note").toString(),Note.class);

        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action))
                {

                    String deviceInfo = "";
                    Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

                    //Intent sınıfından BluetoothDevice nesnesini alıyoruz
                    BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    deviceInfo += btDevice.getName() + "\n" + btDevice.getAddress();
                    btDeviceLis.add(btDevice);

                    if(btDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        deviceInfo += " (Bounded)";
                    }
                    btDeviceList.add(deviceInfo);
                    bluetoothListView.setAdapter(btArrayAdapter);
                    bluetoothListView.refreshDrawableState();
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        Log.d("BluetoothDevice", "Starting discovery...");
        bluetoothAdapter.startDiscovery();


        bluetoothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bd =  btDeviceLis.get(position);
                Thread clientThread = new ClientBTConnection(bd, note);
                clientThread.run();
            }
        });

    }
}
