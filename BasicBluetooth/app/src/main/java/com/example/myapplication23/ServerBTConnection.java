package com.example.myapplication23;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class ServerBTConnection extends Thread {

    private final BluetoothServerSocket serverSocket;
    private static final String serviceName = "BluetoothDataTransfer";

    public ServerBTConnection(BluetoothAdapter btAdapter) {
        Log.d("BluetoothDevice Server", "Bluetooth Server Connection Thread running...");

        BluetoothServerSocket tempServerSocket = null;

        try {
            tempServerSocket = btAdapter.listenUsingRfcommWithServiceRecord(serviceName, UUID.fromString("f4798eca-54ed-49b7-8b12-925b1084aa5a"));
        } catch (IOException e) {
            Log.d("BluetoothDevice Server", "Could not get a BluetoothServerSocket: " + e.toString());
        }

        serverSocket = tempServerSocket;

    }


    @Override
    public void run() {
        BluetoothSocket btSocket = null;
        while(true) {

            try {
                btSocket = serverSocket.accept();
            } catch (IOException e) {
                Log.d("BluetoothDevice Server", "Could not accept an incoming connection.");
                break;
            }

            if(btSocket != null) {
                Log.d("BluetoothDevice", "Socket accepted sucessfully.");
                try {
                    InputStream is = btSocket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytes;
                    while(true)
                    {
                        bytes = is.read(buffer);
                        String incomingMessage = new String(buffer, 0, bytes);
                        Log.d("input", "InputStream: " + incomingMessage);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Log.d("BluetoothDevice Server", "Could not close ServerSocket: " + e.toString());
                }

                break;
            }
        }
    }


    public void cancel(){

        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.d("BluetoothDevice Server", "Could not close connection: " + e.toString());
        }
    }
}