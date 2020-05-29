package com.keydak.hc.service;

import com.keydak.hc.dto.AcsWorkDTO;

public interface IHcDoorAccessService {
    void startAlarmListen(String deviceIp, short devicePort);
    void stopAlarmListen();
    boolean updateAcsWorkData(int userId);
    AcsWorkDTO getAcsWordData(int doorCount, int cardCount, int alarmCount);
}
