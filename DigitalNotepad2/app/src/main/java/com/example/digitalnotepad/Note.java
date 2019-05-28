package com.example.digitalnotepad;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;



public class Note implements Serializable
{
    public int id;
    private String title;
    private double x,y;
    private String description;
    private Date createdDate;
    private NoteColor color;
    private NotePriority priority;

    private ArrayList<Attachment> attachments = new ArrayList<>();

    public Note()
    {
        getID();
        this.title = "";
        this.description = "";
        this.createdDate = new Date();
        this.color = NoteColor.WHITE;
        this.priority = NotePriority.NORMAL;

    }
    public Note(String title, String description, Date createdDate, NoteColor color, NotePriority priority) {
        getID();
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.color = color;
        this.priority = priority;
    }
    public Note(String title, String description, Date createdDate, NoteColor color) {
        getID();
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.color = color;
        this.priority = NotePriority.NORMAL;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public NotePriority getPriority() {
        return priority;
    }

    public void setPriority(NotePriority priority) {
        this.priority = priority;
    }

    public NoteColor getColor() {
        return color;
    }

    public void setColor(NoteColor color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public String getCreatedAgoTime()
    {
        return getAgoTime(createdDate);
    }

    public LatLng getLocation() {
        return new LatLng(x,y);
    }

    public void setLocation(LatLng location) {
        this.x = location.latitude;
        this.y = location.longitude;
    }

    public void getID()
    {
        this.id = App.getSharedPref().getInt("notecounter",0);
        SharedPreferences.Editor editor = App.getSharedPref().edit();
        editor.putInt("notecounter", this.id+1);
        editor.commit();

    }
    public void Save()
    {
        Set<String> noteids = App.getSharedPref().getStringSet("noteids", new HashSet<String>());
        if(!noteids.contains(String.valueOf(this.id))) // daha önceden ekli degilse
        {
            noteids.add(String.valueOf(this.id));
            SharedPreferences.Editor editor = App.getSharedPref().edit();
            editor.putStringSet("noteids", noteids);
            editor.commit();
        }
        Gson gson = new Gson();
        SharedPreferences.Editor editor = App.getSharedPref().edit();
        editor.putString("n"+String.valueOf(this.id),  gson.toJson(this));
        editor.commit();
    }

    public void Delete()
    {
        Set<String> noteids = App.getSharedPref().getStringSet("noteids", new HashSet<String>());
        if(noteids.contains(String.valueOf(this.id))) // daha önceden ekliyse
        {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = App.getSharedPref().edit();
            editor.remove("n"+String.valueOf(this.id));
            noteids.remove(String.valueOf(this.id));
            editor.putStringSet("noteids", noteids);
            editor.commit();
        }
        Gson gson = new Gson();
        SharedPreferences.Editor editor = App.getSharedPref().edit();
        editor.putString(String.valueOf(this.id),  gson.toJson(this));
        editor.commit();
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }

    public static void sort(Note[] notes, Boolean reverse)
    {

        if(reverse)
            Arrays.sort(notes, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    return o1.priority.compareTo(o2.priority);
                }
            });
        else
            Arrays.sort(notes, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    return o2.priority.compareTo(o1.priority);
                }
            });


    }
    public static String getAgoTime(Date past)
    {
        Date now = new Date();
        long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());


        if(seconds<60)
            return seconds + " " + App.getContext().getString(R.string.secondsago);
        else if(minutes<60)
            return minutes + " " + App.getContext().getString(R.string.minutesago);
        else if(hours<24)
            return hours+" " +App.getContext().getString(R.string.hoursago);
        else
            return days +" " +App.getContext().getString(R.string.daysago);

    }


}
