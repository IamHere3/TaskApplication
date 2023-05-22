package com.example.mementomori;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DelayNotificationCreator extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // used https://www.youtube.com/watch?v=750gsBtAsoI and https://stackoverflow.com/questions/34517520/how-to-give-notifications-on-android-on-specific-time
        // to implement delayed notifications
        intent = new Intent(context, NotificationCreator.class);
        context.startService(intent);
    }
}
