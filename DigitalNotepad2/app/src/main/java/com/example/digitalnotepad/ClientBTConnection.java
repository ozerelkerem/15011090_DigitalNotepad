package com.example.digitalnotepad;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class ClientBTConnection extends Thread {

    private Note n;

    private BluetoothSocket btSocket;
    private BluetoothDevice btDevice;
    private static final String uuidVal = "f4798eca-54ed-49b7-8b12-925b1084aa5a";

    public ClientBTConnection(BluetoothDevice device, Note note) {
        Log.d("BluetoothDevice Client", "Bluetooth Client Connection Thread running...");
        n = note;
        BluetoothSocket tempSocket = null;
        btDevice = device;

        try {
            tempSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuidVal));
        } catch (IOException e) {
            Log.d("BluetoothDevice Client", "Could not create RFCOMM socket: " + e.toString());
        }

        btSocket = tempSocket;

    }

    @Override
    public void run() {

        try {
            btSocket.connect();
            OutputStream os = btSocket.getOutputStream();
            os.write(new Gson().toJson(n).getBytes());
        } catch (IOException e) {
            Log.d("BluetoothDevice Client", "Could not connect: " + e.toString());

            try {
                btSocket.close();
            } catch (IOException e1) {
                Log.d("BluetoothDevice Client", "Could not close connection: " + e.toString());
            }
            return;
        }


    }

    public boolean cancel() {

        try {
            btSocket.close();
        } catch (IOException e) {

            Log.d("BluetoothDevice Client", "Could not close connection: " + e.toString());
            return false;

        }

        return true;
    }

}
