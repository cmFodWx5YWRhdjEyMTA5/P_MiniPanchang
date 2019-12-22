package com.applettechnologies.minipanchang.Broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(3,notification);

        SharedPreferences sharedPreferences = context.getSharedPreferences("char",Context.MODE_PRIVATE);
        Boolean chal = sharedPreferences.getBoolean("char",true);
        if(chal == true){

        }
        Boolean labh = sharedPreferences.getBoolean("labh",true);
        if(labh == true){

        }
        Boolean shubh = sharedPreferences.getBoolean("shubh",true);
        if(shubh == true){

        }
        Boolean amrit = sharedPreferences.getBoolean("amrit",true);
        if(amrit == true){

        }
        */
    }
}
