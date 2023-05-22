package com.example.mementomori;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationCreator extends IntentService {
    private static final int notificationID = 1;
    public NotificationCreator() {
        super("NotificationCreator");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Notification.Builder builder = new Notification.Builder(this);

        // only api 26 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String test = "Holder Text";

            Bundle ContentText = intent.getExtras();
            test = ContentText.getString("ContextText");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");

            builder.setSmallIcon(R.drawable.appicon);
            builder.setContentTitle("Daily Reminder to complete your tasks");
            builder.setContentText(test);

            /*
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("context text?"));
            */
            Intent notificationIntent = new Intent(this, AllTasks.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Notification notification = builder.build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        }
    }
}
