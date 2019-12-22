package com.applettechnologies.minipanchang.Service;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.applettechnologies.minipanchang.Activities.MainActivity;
import com.applettechnologies.minipanchang.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.ContentValues.TAG;

public class MPFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.


        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuillder = new NotificationCompat.Builder(this,"SIMPLE")
                .setSmallIcon(R.drawable.astrology_icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1,mBuillder.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
