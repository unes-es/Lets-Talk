package com.applications.coffee.letstalkrussian_free;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

//TODO: optimize NotificationsManager and make sure the notifications does not have the same date

public final class NotificationsManager extends BroadcastReceiver {

    static boolean shouldNotify = false;

    public final static String CHANNEL_ID = "FAFAFA8456AEF4A6E5F4";

    public static void notify(Context context,String title,String content,int id,int delay){

        Intent resultIntent = new Intent(context,context.getClass());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //createNotificationChannel(context);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);
        //final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        //notificationManager.notify(id, mBuilder.build());
        Intent notificationIntent = new Intent(context, NotificationsManager.class);
        notificationIntent.putExtra( "NOTIFICATION_ID", id);
        notificationIntent.putExtra("NOTIFICATION", mBuilder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    public static void createNotificationChannel(Context c,String ch) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "c.getString(R.string.channel_name)";
            String description = "c.getString(R.string.channel_description)";
            NotificationChannel channel = new NotificationChannel(ch, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = c.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = intent.getParcelableExtra("NOTIFICATION");
        int id = intent.getIntExtra("NOTIFICATION_ID", 0);
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
    }

    /*class EveryDayNotification{
        int ID = 88812416;
        String title = "";
        String content = "";
   }*/
}
