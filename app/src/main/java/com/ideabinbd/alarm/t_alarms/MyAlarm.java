package com.ideabinbd.alarm.t_alarms;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ramim on 1/21/2018.
 */

public class MyAlarm extends RealmObject {

    @PrimaryKey
    int id;

    String name, type, time;

    boolean state;

    public MyAlarm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
