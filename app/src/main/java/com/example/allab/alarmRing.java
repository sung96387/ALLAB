package com.example.allab;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class alarmRing extends Service {
    public alarmRing() {
    }
    private MediaPlayer mediaPlayer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //    @Override
//    public void onCreate(Intent intent){
////        super.onCreate();
////        String temp = intent.getExtras().getString("drug_name");
////        if (Build.VERSION.SDK_INT >= 26) {
////            String CHANNEL_ID = "default";
////            int importance = NotificationManager.IMPORTANCE_HIGH;
////            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
////                    "Channel human readable title",
////                    importance);
////
////            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
////
////            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
////                    .setContentTitle("ALLAB")
////                    .setContentText(temp + " 약을 복용할 시간입니다.")
////                    .setSmallIcon(R.mipmap.ic_launcher)
////
////                    .build();
////
////
////            startForeground(1, notification);
//        }
//
//    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        Log.d("test","servedsafsdf123456789");
        String temp = intent.getExtras().getString("drug_name");
        Toast.makeText(this,temp+" 약을 복용할 시간입니다.",Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelID = "channel_01"; //알림채널 식별자
            String channelName = "MyChannel01"; //알림채널의 이름(별명)

            //알림채널 객체 만들기
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);

            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성
            builder = new NotificationCompat.Builder(this, channelID);
        }else{
            //알림 건축가 객체 생성
            builder= new NotificationCompat.Builder(this, null);
        }
        builder.setSmallIcon(android.R.drawable.btn_star);
        builder.setContentTitle("ALLAB");//알림창 제목
        builder.setContentText(temp+" 약을 복용할 시간입니다.");//알림창 내용

        //알림창을 클릭시에 실행할 작업(SecondActivity 실행) 설정
        Intent intentT= new Intent(this,drug_alarm.class);
        //지금 실행하는 것이 아니라 잠시 보류시키는 Intent 객체 필요
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0,intentT,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.beep);
        mediaPlayer.start();
        builder.setVibrate(new long[]{0, 2000, 1000, 3000});
        //알림창 클릭시에 자동으로 알림제거
        builder.setAutoCancel(true);


        Notification notification = builder.build();
        notificationManager.notify(1,notification);



//        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(001,getNotificationBuilder(temp).build());
        return START_NOT_STICKY;
    }
//    private NotificationCompat.Builder getNotificationBuilder(String temp)
//    {
//        Intent notificationIntent = new Intent(this,drug_alarm.class);
//        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this,"drug alarm")
//                .setSmallIcon(android.R.drawable.btn_star)
//                .setContentTitle("ALLAB")
//                .setContentText(temp+" 약을 복용할 시간입니다.")
//                .setContentIntent(notificationPendingIntent)
//                .setAutoCancel(true);
//        return mbuilder;
//    }


}