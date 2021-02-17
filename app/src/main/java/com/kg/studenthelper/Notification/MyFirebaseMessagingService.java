package com.kg.studenthelper.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kg.studenthelper.Activity.PendingActivity;
import com.kg.studenthelper.Activity.StudentNoticeActivity;
import com.kg.studenthelper.Activity.SupervisorPendingActivity;
import com.kg.studenthelper.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_1_ID = "channel1";
   // public static final String CHANNEL_2_ID = "channel2";
    String title,message,type,building;
    String types="complain";
    String update="update";
    String notice="notice";
    Intent intent;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");
        type=remoteMessage.getData().get("type");
        building=remoteMessage.getData().get("building");



        notificationManager = NotificationManagerCompat.from(this);

        if(type.equals(types)) {

            intent=new Intent(this,SupervisorPendingActivity.class);
            intent.putExtra("building",building);

        }
        else if(type.equals(update))
        {
            intent=new Intent(this, PendingActivity.class);
            intent.putExtra("building",building);
        }
        else if(type.equals(notice))
        {
            intent=new Intent(this, StudentNoticeActivity.class);
            intent.putExtra("building",building);
        }



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_1_ID,
                        "Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("This is Channel 1");
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel1);


                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


               // Intent intent = new Intent(this, SupervisorPendingActivity.class);



                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setSound(alarmSound)
                        .setVibrate(pattern)
                        .build();
                notificationManager.notify(1, notification);


            } else {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setContentIntent(pendingIntent);
                        ;
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, builder.build());
            }

        /*
        else
        {

            Log.d("FROM", remoteMessage.getFrom());
        }*/


    }
}
