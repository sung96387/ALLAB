package com.example.allab;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String get_your_string = intent.getExtras().getString("drug_name");
        Intent service_intent = new Intent(context,alarmRing.class);
        Log.d("test","servedsafsdf");

        service_intent.putExtra("drug_name",get_your_string);
        context.startService(service_intent);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//            this.context.startForegroundService(service_intent);
//        }else{
//            this.context.startService(service_intent);
//        }
    }
}