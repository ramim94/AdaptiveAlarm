package com.ideabinbd.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Ramim on 1/20/2018.
 */

public class AlarmSetter {
    Context context, applicationContext;
    AlarmManager alarmManager;
    Calendar calendar;
    Intent hereToAlarmStopper;
    public AlarmSetter(Context context) {
        this.context= context;
        applicationContext=context.getApplicationContext();
        hereToAlarmStopper= new Intent(applicationContext, AlarmStopper.class);
    }

    public void setCalendar(String time) {
        calendar=Calendar.getInstance();
        int hourOfDay,minute;
        hourOfDay= Integer.parseInt(time.substring(0,time.indexOf(":")));
        minute= Integer.parseInt(time.substring(time.indexOf(":")+1));
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);

    }

    public void setOnce(int RequestCode){
        hereToAlarmStopper.setAction(Intent.ACTION_MAIN);
        hereToAlarmStopper.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        hereToAlarmStopper.putExtra("info",RequestCode);

        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                applicationContext, RequestCode, hereToAlarmStopper, PendingIntent.FLAG_UPDATE_CURRENT);

        int alarmType = AlarmManager.RTC_WAKEUP;
        alarmManager.set(alarmType, calendar.getTimeInMillis(),pendingIntent);


        Log.d("AlarmStatus", "Alarm "+String.valueOf(RequestCode)+" set");
    }

    public void setRepeating(int RequestCode){

        hereToAlarmStopper.setAction(Intent.ACTION_MAIN);
        hereToAlarmStopper.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        hereToAlarmStopper.putExtra("info",RequestCode);

        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                applicationContext, RequestCode, hereToAlarmStopper, PendingIntent.FLAG_UPDATE_CURRENT);

        int alarmType = AlarmManager.RTC_WAKEUP;
        final int ONE_DAY = 1000*60*60*24;

        alarmManager.setRepeating(alarmType, calendar.getTimeInMillis(),
                ONE_DAY, pendingIntent);

        Log.d("AlarmStatus", "Alarm "+String.valueOf(RequestCode)+" set");
    }

    public void cancelAlarm(int requestCode){
        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        hereToAlarmStopper.setAction(Intent.ACTION_MAIN);
        hereToAlarmStopper.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        hereToAlarmStopper.putExtra("info",requestCode);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                applicationContext, requestCode, hereToAlarmStopper,     PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        Log.d("AlarmStatus", "Alarm "+String.valueOf(requestCode)+" cancelled");
    }

}
