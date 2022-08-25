package com.android.store;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    public  static  final String CHANNEL_ID="channel_service_example";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    public void createChannelNotification()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel Service Example",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager!= null) {
                manager.createNotificationChannel(channel);

            }
        }
    }
}
