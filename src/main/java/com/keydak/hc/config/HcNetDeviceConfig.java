package com.keydak.hc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Scope("singleton")
@ConfigurationProperties(prefix = "hik")
public class HcNetDeviceConfig {
    private List<DeviceInfo> doors;
    private List<VideoInfo> videos;
    public List<DeviceInfo> getDoors() {
        return doors;
    }
    public List<VideoInfo> getVideos() {
        return videos;
    }

    public static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setDoors(List<DeviceInfo> doors) {
        this.doors = doors;
    }

    public void setVideos(List<VideoInfo> videos) {
        this.videos = videos;
    }

    public VideoInfo getVideos(String deviceIp, String port){
        if (deviceIp == null || port == null) return null;
        for (VideoInfo info :
                this.videos) {
            if (deviceIp.equals(info.getDeviceIp()) && port.equals(info.getPort())) return info;
        }
        return null;
    }

    public static class DeviceInfo {
        private String deviceId;
        private String deviceIp;
        private String port;
        private String userName;
        private String password;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDeviceIp() {
            return deviceIp;
        }

        public void setDeviceIp(String deviceIp) {
            this.deviceIp = deviceIp;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    public static class AlarmInfo{
        private String name = "区域入侵告警";
        private Integer type = 3;
        private String typeDescription = "安全";
        private Integer level = 2;
        private String levelDescription = "告警";
        private String content = "区域入侵告警";
        private String shortContent = "区域入侵告警";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getTypeDescription() {
            return typeDescription;
        }

        public void setTypeDescription(String typeDescription) {
            this.typeDescription = typeDescription;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getLevelDescription() {
            return levelDescription;
        }

        public void setLevelDescription(String levelDescription) {
            this.levelDescription = levelDescription;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getShortContent() {
            return shortContent;
        }

        public void setShortContent(String shortContent) {
            this.shortContent = shortContent;
        }
    }

    public static class VideoInfo extends DeviceInfo{
        private AlarmInfo alarmInfo;

        public AlarmInfo getAlarmInfo() {
            return alarmInfo;
        }

        public void setAlarmInfo(AlarmInfo alarmInfo) {
            this.alarmInfo = alarmInfo;
        }
    }
}
