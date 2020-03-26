package com.keydak.hc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "HcNetEventInfo")
public class EventInfo extends BaseEntity{

    private int majorType;
    private int minorType;
    private Date alarmTime;
    private String netUser;
    private int channelNo;
    private String remoteHostAdd;

    public int getMajorType() {
        return majorType;
    }

    public void setMajorType(int majorType) {
        this.majorType = majorType;
    }

    public int getMinorType() {
        return minorType;
    }

    public void setMinorType(int minorType) {
        this.minorType = minorType;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getNetUser() {
        return netUser;
    }

    public void setNetUser(String netUser) {
        this.netUser = netUser;
    }


    public int getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public String getRemoteHostAdd() {
        return remoteHostAdd;
    }

    public void setRemoteHostAdd(String remoteHostAdd) {
        this.remoteHostAdd = remoteHostAdd;
    }
}
