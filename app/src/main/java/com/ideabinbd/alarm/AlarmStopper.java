package com.ideabinbd.alarm;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ideabinbd.alarm.t_alarms.MyAlarm;

import io.realm.Realm;

public class AlarmStopper extends AppCompatActivity {
    Button alarmStopper;
    Ringtone ringtone;
    MyAlarm thisAlarm;
    Realm realm;
    int alarmID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_stopper);
        realm= Realm.getDefaultInstance();
        thisAlarm=null;

        alarmStopper= findViewById(R.id.alarm_stopper);
       try{
           alarmID= getIntent().getExtras().getInt("info");
           thisAlarm= realm.where(MyAlarm.class).equalTo("id",alarmID).findFirst();
       }catch (NullPointerException e){
           Log.d("info",e.toString());
       }

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        ringtone= RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();

        alarmStopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                if(thisAlarm!=null){
                    if(!thisAlarm.getType().equals("Everyday")){
                        realm.beginTransaction();
                        thisAlarm.setState(false);
                        realm.commitTransaction();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
