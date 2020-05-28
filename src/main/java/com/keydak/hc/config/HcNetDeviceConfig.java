package com.keydak.hc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "hik")
public class HcNetDeviceConfig {
    private static List<DeviceInfo> doors;
    private static List<DeviceInfo> videos;

    public static List<DeviceInfo> getDoors() {
        return doors;
    }
    public static List<DeviceInfo> getVideos() {
        return videos;
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
