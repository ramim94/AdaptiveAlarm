package com.ideabinbd.alarm.adapter;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ideabinbd.alarm.AlarmSetter;
import com.ideabinbd.alarm.R;
import com.ideabinbd.alarm.t_alarms.MyAlarm;

import io.realm.MyAlarmRealmProxy;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by Ramim on 1/23/2018.
 */

public class AlarmRecyclerAdapter extends RealmRecyclerViewAdapter<MyAlarm,EachAlarm> {

    OrderedRealmCollection<MyAlarm> results;

    public AlarmRecyclerAdapter(@Nullable OrderedRealmCollection<MyAlarm> data, boolean autoUpdate) {
        super(data, autoUpdate);
        setHasStableIds(true);
        results= data;
    }

    @Override
    public EachAlarm onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_alarm_show, parent, false);
        return new EachAlarm(itemView,results);
    }

    @Override
    public void onBindViewHolder(EachAlarm holder, int position) {
        MyAlarm obj = getItem(position);
        holder.alarmTime.setText(obj.getTime());
        holder.alarmName.setText(obj.getName());
        holder.alarmType.setText(obj.getType());
        holder.alarmState.setChecked(obj.isState());
    }

    @Override
    public long getItemId(int position) {
        return results.get(position).getId();
    }
}


class EachAlarm extends RecyclerView.ViewHolder{
    TextView alarmTime,alarmName,alarmType;
    ToggleButton alarmState;
    OrderedRealmCollection<MyAlarm> results;
    Context context;
    Realm realm;
    AlarmSetter alarmSetter;

    public EachAlarm(View itemView, final OrderedRealmCollection<MyAlarm> results) {
        super(itemView);
        alarmTime= itemView.findViewById(R.id.each_alarm_time);
        alarmName= itemView.findViewById(R.id.each_alarm_name);
        alarmState= itemView.findViewById(R.id.each_alarm_switch);
        alarmType= itemView.findViewById(R.id.each_alarm_type);
        this.results= results;
        this.context= itemView.getContext();
        realm= Realm.getDefaultInstance();
        alarmSetter= new AlarmSetter(context);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int id= results.get(getAdapterPosition()).getId();

                AlertDialog.Builder confirmDel= new AlertDialog.Builder(context);
                confirmDel.setTitle("Delete").setMessage("Sure you want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final RealmResults<MyAlarm> realmResults= realm.where(MyAlarm.class).equalTo("id",id).findAll();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        alarmSetter.cancelAlarm(id);
                                        realmResults.deleteAllFromRealm();

                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                confirmDel.show();
                return true;
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id= results.get(getAdapterPosition()).getId();
                final MyAlarm realmResults= realm.where(MyAlarm.class).equalTo("id",id).findFirst();

                //creating views
                AlertDialog.Builder update= new AlertDialog.Builder(context);
                update.setTitle("Update");
                LinearLayout layout= new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

                final EditText name;
                RadioGroup rg;
                final RadioButton everyday,oneTime;
                final TextView time;
                Button save;

                time=new TextView(context);
                time.setTextSize(20);


                name= new EditText(context);
                name.setHint("Change alarm name");

                rg=new RadioGroup(context);
                everyday= new RadioButton(context);
                oneTime= new RadioButton(context);
                everyday.setText("Everyday");
                everyday.setId(R.id.rb1Id);
                oneTime.setText("One Time");
                oneTime.setId(R.id.rb2Id);

                rg.addView(everyday);
                rg.addView(oneTime);

                //setting old values
                String oldTime=realmResults.getTime();
                time.setText(oldTime);
                final int hourOfDay,minute;
                hourOfDay= Integer.parseInt(oldTime.substring(0,oldTime.indexOf(":")));
                minute= Integer.parseInt(oldTime.substring(oldTime.indexOf(":")+1));
                if(realmResults.getType().equals("Everyday")){
                    everyday.setChecked(true);
                }else{
                    oneTime.setChecked(true);
                }
                name.setText(realmResults.getName());

                save=new Button(context);
                save.setText("Update");

                //adding views to linear layout
                layout.addView(name);
                layout.addView(rg);
                layout.addView(time);
                layout.addView(save);

                update.setView(layout);

                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TimePickerDialog tpd= new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                String newTime=String.valueOf(i)+":"+String.valueOf(i1);
                                    time.setText(newTime);

                            }
                        },hourOfDay,minute,false);
                        tpd.show();
                    }
                });

                update.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                String newTime=time.getText().toString();
                                alarmSetter.setCalendar(newTime);
                                realmResults.setTime(newTime);
                                realmResults.setName(name.getText().toString());
                                if(everyday.isChecked()){
                                    realmResults.setType("Everyday");
                                }else{
                                    realmResults.setType("One Time");
                                }
                                if(realmResults.isState()){
                                    if(everyday.isChecked()){
                                        alarmSetter.setRepeating(realmResults.getId());
                                    }else{
                                        alarmSetter.setOnce(realmResults.getId());
                                    }
                                }
                            }
                        });
                        dialogInterface.dismiss();
                    }
                });

                update.show();
            }
        });

        alarmState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyAlarm alarm= realm.where(MyAlarm.class).equalTo("id",results.get(getAdapterPosition()).getId()).findFirst();

                if(alarmState.isChecked()){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            alarm.setState(true);
                            if(alarmType.equals("Everyday")){
                                alarmSetter.setRepeating(alarm.getId());
                            }else{
                                alarmSetter.setOnce(alarm.getId());
                            }
                        }
                    });
                }else{
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            alarm.setState(false);
                            alarmSetter.cancelAlarm(alarm.getId());
                        }
                    });
                }
            }
        });
    }

}