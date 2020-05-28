package com.keydak.hc.service;

import com.keydak.hc.enums.HikSetUpAlarmEnum;

public interface IHikService {
    int login(String m_sDeviceIP, short m_sPort, String m_sUsername, String m_sPassword);
    boolean logout(int userId);

    int setUpAlarmChan(
            int userId,
            HikSetUpAlarmEnum.Level level,
            HikSetUpAlarmEnum.AlarmInfoType alarmInfoType,
            HikSetUpAlarmEnum.DeployType deployType
    );

    boolean closeAlarmChan(int alarmChanId);
//    boolean setCallBack();
//    boolean clearUp();

}
