package com.keydak.hc.service;

public interface ISmtService {
    void sendSMS(String host, int port, String number, String content);
    void sendVoice(String host, int port, String number, String content);
}
