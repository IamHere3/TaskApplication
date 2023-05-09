package com.example.mementomori;

import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationCreator extends SaveClass {

    protected void onCreate()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");

        builder.setSmallIcon(R.drawable.appicon);
        builder.setContentTitle("Daily Reminder to complete your tasks");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this, MainActivity.class);
    }
}
