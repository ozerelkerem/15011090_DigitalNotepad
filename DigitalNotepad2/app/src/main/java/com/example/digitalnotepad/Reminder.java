package com.example.digitalnotepad;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Reminder implements Serializable {

    private int noteid;
    public int id;
    private Date date;
    private double x,y;
    private double locradius=500;
    private ReminderType type;

    public Reminder(Date date) {
        getID();
        this.date = date;
        this.type = ReminderType.DATETIMEBASED;
    }

    public void setNote(Note n)
    {
        noteid = n.id;
    }

    public Note getNote()
    {
        return App.getNote(noteid);
    }

    public ReminderType getType() {
        return type;
    }

    public void setType(ReminderType type) {
        this.type = type;
    }

    public double getLocradius() {
        return locradius;
    }

    public void setLocradius(double locradius) {
        this.locradius = locradius;
    }

    public LatLng getLocation() {
        return new LatLng(x,y);
    }

    public void setLocation(LatLng location) {
        this.x = location.latitude;
        this.y = location.longitude;
    }

    public Date getDate() {
        return date;
    }
    public String getTimeS() {
        return (new SimpleDateFormat("h:mm a")).format(date);
    }
    public String getDateS() {
        return (new SimpleDateFormat("EEE, MMM d, ''yy")).format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public void getID()
    {
        this.id = App.getSharedPref().getInt("remindercounter",0);
        SharedPreferences.Editor editor = App.getSharedPref().edit();
        editor.putInt("remindercounter", this.id+1);
        editor.commit();

    }

    public void Save()
    {
        Set<String> noteids = App.getSharedPref().getStringSet("reminderids", new HashSet<String>());
        if(!noteids.contains(String.valueOf(this.id))) // daha önceden ekli degilse
        {
            noteids.add(String.valueOf(this.id));
            SharedPreferences.Editor editor = App.getSharedPref().edit();
            editor.putStringSet("reminderids", noteids);
            editor.commit();
        }
        Gson gson = new Gson();
        SharedPreferences.Editor editor = App.getSharedPref().edit();
        editor.putString("r"+String.valueOf(this.id),  gson.toJson(this));
        editor.commit();
    }

    public void Delete()
    {
        Set<String> noteids = App.getSharedPref().getStringSet("reminderids", new HashSet<String>());
        if(noteids.contains(String.valueOf(this.id))) // daha önceden ekliyse
        {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = App.getSharedPref().edit();
            editor.remove("r"+String.valueOf(this.id));
            noteids.remove(String.valueOf(this.id));
            editor.putStringSet("reminderids", noteids);
            editor.commit();
        }
        Gson gson = new Gson();
        SharedPreferences.Editor editor = App.getSharedPref().edit();
        editor.putString(String.valueOf(this.id),  gson.toJson(this));
        editor.commit();

        ((NotificationManager)App.getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(this.id);
    }
}
