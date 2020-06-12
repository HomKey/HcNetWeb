package com.keydak.hc.service;

public interface ISmtService {
    boolean sendSMS(String number, String content);
    boolean sendVoice(String number, String content);
}
