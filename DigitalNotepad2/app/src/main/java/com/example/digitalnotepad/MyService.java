package com.example.digitalnotepad;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.Date;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "tagtst";
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();

    public static final String ACTION_LOCATION_BROADCAST = "tagtstLocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {


                    Reminder[] reminders = App.getReminders();
                    if (reminders != null)
                        for (Reminder r : reminders) {
                            switch (r.getType()) {
                                case LOCATIONBASED:
                                    break;
                                case DATETIMEBASED: {
                                    Calendar calendar = Calendar.getInstance();
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar.setTime(r.getDate());
                                    calendar2.setTime(new Date());

                                    Long distinmilis = calendar2.getTimeInMillis() - calendar.getTimeInMillis();
                                    if (distinmilis > 0 && distinmilis <= 1000 * 60 * 5) {
                                        App.showNotification(r);
                                    }


                                }
                                break;
                                case TIMEBASED: {
                                    Calendar calendar = Calendar.getInstance();
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar.setTime(r.getDate());
                                    calendar2.setTime(new Date());

                                    int x = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                                    int y = calendar2.get(Calendar.HOUR_OF_DAY) * 60 + calendar2.get(Calendar.MINUTE);
                                    int distinmilis = (y - x) * 60 * 1000;
                                    if (distinmilis > 0 && distinmilis <= 1000 * 60 * 5) {
                                        App.showNotification(r);
                                    }

                                }
                                break;
                            }

                        }

                    try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(1000*1);


        int priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();


        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {
        Reminder[] reminders = App.getReminders();
        if (reminders != null)
            for (Reminder r : reminders) {
                switch (r.getType()) {
                    case LOCATIONBASED: {
                        Location target = new Location("");
                        target.setLatitude(r.getLocation().latitude);
                        target.setLongitude(r.getLocation().longitude);
                        if(location.distanceTo(target) < r.getLocradius())
                            App.showNotification(r);
                    }break;


                }
            }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest,  this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
