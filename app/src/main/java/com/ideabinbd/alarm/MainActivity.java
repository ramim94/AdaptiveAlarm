package com.ideabinbd.alarm;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ideabinbd.alarm.adapter.AlarmRecyclerAdapter;
import com.ideabinbd.alarm.t_alarms.MyAlarm;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    AlarmRecyclerAdapter rvA;
    Realm instance;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: Everytime alarm created or edited set alarm
        instance= Realm.getDefaultInstance();

        rv= findViewById(R.id.all_alarm_recycler);
        fab= findViewById(R.id.main_ac_fab);
        OrderedRealmCollection<MyAlarm> dataToSend=instance.where(MyAlarm.class).findAll();
        rvA= new AlarmRecyclerAdapter(dataToSend,true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(rvA);
        rv.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddAlarm.class));
            }
        });

        /*
        alarmId= findViewById(R.id.alarm_id);
        alarmActivate= findViewById(R.id.btn_alarm_setter);

        alarmCancel= findViewById(R.id.text_alarmCancel);
        alarmInfo= findViewById(R.id.text_alarminfo);

        controller= new AlarmSetter(this);

        try{
            String info=getIntent().getExtras().getString("info");
            if (info!=null){
                Log.d("info","alarm running");
                alarmInfo.setText(info);
            }
        }catch (NullPointerException e){
            Log.d("info","null pointer in info");
        }


        alarmActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int alarmID= Integer.parseInt(alarmId.getText().toString());
                controller.setRepeating(alarmID);
            }
        });

        alarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int alarmID= Integer.parseInt(alarmId.getText().toString());
                controller.cancelAlarm(alarmID);
            }
        });

        */
    }

    @Override
    protected void onDestroy() {
        instance.close();
        super.onDestroy();
    }
}
