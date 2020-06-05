package com.keydak.hc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    private String host;
    private String port;
    private String[] numberArray;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String[] getNumberArray() {
        return numberArray;
    }

    public void setNumberArray(String[] numberArray) {
        this.numberArray = numberArray;
    }
}
