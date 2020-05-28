package com.keydak.hc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("singleton")
@ConfigurationProperties(prefix = "hik")
public class HcNetDeviceConfig {
    private List<DeviceInfo> doors;
    private List<DeviceInfo> videos;
    public List<DeviceInfo> getDoors() {
        return doors;
    }
    public List<DeviceInfo> getVideos() {
        return videos;
    }

    public void setDoors(List<DeviceInfo> doors) {
        this.doors = doors;
    }

    public void setVideos(List<DeviceInfo> videos) {
        this.videos = videos;
    }

    public static class DeviceInfo {
        private String deviceIp;
        private String port;
        private String userName;
        private String password;

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
    }
}
