package com.pennshape.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.pennshape.app.R;
import com.pennshape.app.activity.PSLoginActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PSAlertService extends Service {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static String NewMessageAction = "com.pennshape.app.intent.action.alert";
    public static String IntentExtra = "com.pennshape.app.intent.extra.alert";
    public static int notificationID  = 82110242; //4e4e722
    public PSAlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PSAlertService", "onStartCommand~");
        String message = alertMessage();
        if (message!=null) {
            notifyUserAction(message);
        }
        return Service.START_NOT_STICKY;
    }

    private void notifyUserAction(String message){
        //Builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.sport_yg);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sport_yg);
            mBuilder.setLargeIcon(largeIcon);
        } else{
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(largeIcon);
        }
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        mBuilder.setContentTitle("Penn Fit");
        mBuilder.setContentText(message);
        mBuilder.setVibrate(new long[] { 300, 300, 300, 300, 300 });
        mBuilder.setAutoCancel(true);
        //Intent
        Intent resultIntent = new Intent(this, PSLoginActivity.class);
        resultIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ps_service_alert_intent_extra));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        //NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, mBuilder.build());
    }

    public boolean commitedToday(){
        Calendar today = Calendar.getInstance();
        String dateString = dateFormat.format(today.getTime());
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.ps_pref_name), Context.MODE_PRIVATE);
        String lastCommitDate = sharedPref.getString(getString(R.string.ps_pref_key_last_commit), null);
        if(lastCommitDate!=null && lastCommitDate.equals(dateString)) {
            return true;
        }
        return false;
    }

    private String alertMessage(){
        Calendar now = Calendar.getInstance();
        String message = null;
        if(now.get(Calendar.HOUR_OF_DAY)<12){
            message = getString(R.string.ps_service_alert_message_morning);
        }else{
            if(!commitedToday()) {
                message = getString(R.string.ps_service_alert_message_evening);
            }
        }
        return message;
    }
}
