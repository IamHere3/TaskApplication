package com.example.mementomori;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationCreator extends SaveClass {

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void CreateNotification()
    {
        Intent intent = new Intent(this, NotificationCreator.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // only api 26 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");

            builder.setSmallIcon(R.drawable.appicon);
            builder.setContentTitle("Daily Reminder to complete your tasks");
            builder.setContentText("context text?");

            /*
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("context text?"));
            */

            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        }
    }

    public void test()
    {
        Toast test = Toast.makeText(this,"testing", Toast.LENGTH_LONG);
        test.show();

    }
}
