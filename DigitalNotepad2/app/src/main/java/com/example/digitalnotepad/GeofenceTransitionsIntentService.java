package com.example.digitalnotepad;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;


public class GeofenceTransitionsIntentService extends IntentService {


    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }
/*WORKS WHEN SOME APP USE GPS*/


    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {

            Log.d("loc", "error");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.


            // Send notification and log the transition details.
           // sendNotification(geofenceTransitionDetails);
            Log.d("loc",  String.valueOf(geofenceTransition));
        } else {
            // Log the error.
            Log.d("loc", "invalid");
        }
    }


}
