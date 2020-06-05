package com.keydak.hc.entity;

import com.keydak.hc.config.HcNetDeviceConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AlarmData")
public class DcimAlarmInfo {
    @Id
    @Column(name = "Id")
    private String id;
    @Column(name = "Name")
    private String name;
    @Column(name = "EventId")
    private int eventId;
    @Column(name = "DeviceId")
    private String deviceId;
    @Column(name = "DeviceName")
    private String deviceName;
    @Column(name = "AlarmType")
    private int alarmType;
    @Column(name = "AlarmTypeDescription")
    private String alarmTypeDescription;
    @Column(name = "AlarmLevel")
    private int alarmLevel;
    @Column(name = "AlarmLevelDescription")
    private String alarmLevelDescription;
    @Column(name = "AlarmContent")
    private String alarmContent;
    @Column(name = "AlarmShortContent")
    private String alarmShortContent;
    @Column(name = "DetailStore")
    private String detailStore;
    @Column(name = "AlarmTime")
    private Date alarmTime;

    public void setAlarmInfo(HcNetDeviceConfig.AlarmInfo alarmInfo){
        this.eventId = 0;
        this.name = alarmInfo.getName();
        this.alarmType = alarmInfo.getType();
        this.alarmTypeDescription = alarmInfo.getTypeDescription();
        this.alarmLevel = alarmInfo.getLevel();
        this.alarmLevelDescription = alarmInfo.getLevelDescription();
        this.alarmContent = this.deviceName + ":" + alarmInfo.getContent() + ",时间" + HcNetDeviceConfig.SIMPLE_DATE_FORMAT.format(this.alarmTime) + "";
        this.alarmShortContent = alarmInfo.getShortContent();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmTypeDescription() {
        return alarmTypeDescription;
    }

    public void setAlarmTypeDescription(String alarmTypeDescription) {
        this.alarmTypeDescription = alarmTypeDescription;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmLevelDescription() {
        return alarmLevelDescription;
    }

    public void setAlarmLevelDescription(String alarmLevelDescription) {
        this.alarmLevelDescription = alarmLevelDescription;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAlarmShortContent() {
        return alarmShortContent;
    }

    public void setAlarmShortContent(String alarmShortContent) {
        this.alarmShortContent = alarmShortContent;
    }

    public String getDetailStore() {
        return detailStore;
    }

    public void setDetailStore(String detailStore) {
        this.detailStore = detailStore;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }
}
