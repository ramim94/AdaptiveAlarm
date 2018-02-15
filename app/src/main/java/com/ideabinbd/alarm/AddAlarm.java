package com.ideabinbd.alarm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ideabinbd.alarm.t_alarms.MyAlarm;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddAlarm extends AppCompatActivity {
    Realm realm;
    EditText alarmName;
    RadioButton alarmTypeE,alarmTypeO;
    TextView alarmTime;
    Button saveAlarm;
    AlarmSetter alarmSetter;
    Calendar userDefinedAlarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        realm= Realm.getDefaultInstance();
        userDefinedAlarmTime= Calendar.getInstance();
        alarmSetter= new AlarmSetter(this);
        alarmName= findViewById(R.id.input_alarm_name);
        alarmTypeE= findViewById(R.id.input_alarm_type_evd);
        alarmTypeO= findViewById(R.id.input_alarm_type_ot);
        alarmTime= findViewById(R.id.input_set_time);
        saveAlarm= findViewById(R.id.button_alarm_save);




        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog tpd= new TimePickerDialog(AddAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        userDefinedAlarmTime.set(Calendar.HOUR_OF_DAY,i);
                        userDefinedAlarmTime.set(Calendar.MINUTE,i1);
                        String time= String.valueOf(i)+":"+String.valueOf(i1);
                        alarmTime.setText(time);
                    }
                },hour,minute,false);
                setTitle("Set Time");
                tpd.show();
            }
        });

        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txtAlarmTime, txtAlarmName, txtAlarmType;
                txtAlarmName= alarmName.getText().toString();
                txtAlarmTime= alarmTime.getText().toString();
                if(alarmTypeE.isChecked()){
                    txtAlarmType="Everyday";
                }else{
                    txtAlarmType="One Time";
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //todo: create alarm in alarmmanager
                        Number currentIdNum = realm.where(MyAlarm.class).max("id");

                        int nextId;
                        if(currentIdNum == null) {
                            nextId = 1;
                        } else {
                            nextId = currentIdNum.intValue() + 1;
                        }
                        Log.d("debug",String.valueOf(nextId));
                        MyAlarm alarm= realm.createObject(MyAlarm.class,nextId);
                        alarm.setName(txtAlarmName);
                        alarm.setTime(txtAlarmTime);
                        alarm.setType(txtAlarmType);
                        alarm.setState(true);
                        alarmSetter.setCalendar(txtAlarmTime);
                        if (alarmTypeE.isChecked()){
                            alarmSetter.setRepeating(nextId);
                        }else{
                            alarmSetter.setOnce(nextId);
                        }
                    }
                });

                startActivity(new Intent(AddAlarm.this,MainActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
