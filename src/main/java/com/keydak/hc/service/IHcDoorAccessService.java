package com.keydak.hc.service;

import com.keydak.hc.core.structure.AcsWorkStatus;
import com.keydak.hc.dto.AcsWorkDTO;

public interface IHcDoorAccessService {
    int login(String m_sDeviceIP, short m_sPort, String m_sUsername, String m_sPassword);
    void logout();

    void setupAlarmChan();
    void closeAlarmChan();

    void startAlarmListen(String deviceIp, short devicePort);
    void stopAlarmListen();

    boolean updateAcsWorkData();

    AcsWorkDTO getAcsWordData(int doorCount, int cardCount, int alarmCount);
}
