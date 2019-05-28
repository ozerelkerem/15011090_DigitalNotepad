package com.example.digitalnotepad;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.ArrayRes;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
    public static SharedPreferences getSharedPref(){ return mContext.getSharedPreferences("default", MODE_PRIVATE); }

    public static Note getNote(int id)
    {
        Gson gson = new Gson();
        return gson.fromJson(App.getSharedPref().getString("n"+String.valueOf(id),""), Note.class);
    }
    public static Note[] getNotes()
    {
        Gson gson = new Gson();


        Set<String> noteids = App.getSharedPref().getStringSet("noteids", new HashSet<String>());
        if(noteids.size() <= 0)
            return null;
        Note[] list = new Note[noteids.size()];

        int i=0;
        for (String id: noteids)
            list[i++] = gson.fromJson(App.getSharedPref().getString("n"+id,""), Note.class);


        return list;
    }

    public static Reminder[] getReminders()
    {
        Gson gson = new Gson();


        Set<String> noteids = App.getSharedPref().getStringSet("reminderids", new HashSet<String>());
        if(noteids.size() <= 0)
            return null;
        Reminder[] list = new Reminder[noteids.size()];

        int i=0;
        for (String id: noteids)
            list[i++] = gson.fromJson(App.getSharedPref().getString("r"+id,""), Reminder.class);


        return list;
    }

    public static void showNotification(Reminder reminder) {

        Intent in = new Intent(getContext(), editreminderActivity.class);
        in.putExtra("reminder", reminder);

        PendingIntent contentIntent =
                PendingIntent.getActivity(getContext(), 0, in, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bell);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
        mBuilder.setContentTitle(reminder.getNote().getTitle());
        mBuilder.setContentText(reminder.getNote().getDescription());
        mBuilder.setSmallIcon(R.drawable.bell);
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setWhen(System.currentTimeMillis());

        mBuilder.setSubText(App.getContext().getResources().getStringArray(R.array.remindertypes)[Arrays.asList(ReminderType.values()).indexOf(reminder.getType())]);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(reminder.getNote().getDescription())
                .setBigContentTitle(reminder.getNote().getTitle()));


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);

        long[] v = {500, 1000};
        mBuilder.setVibrate(v);

        // NotificationManager nesnesi oluşturuyoruz.
        NotificationManager notificationManager = (NotificationManager)
                getContext().getSystemService(NOTIFICATION_SERVICE);
        // NotificationManager ile bildirimi inşa ediyoruz.
        notificationManager.notify(reminder.id, mBuilder.build());


    }
}