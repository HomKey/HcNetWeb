package com.keydak.hc.service;

import com.keydak.hc.core.structure.AcsWorkStatus;
import com.keydak.hc.dto.AcsWorkDTO;

public interface IRealTimeDataService {
    void saveAcsWorkData(AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50 acsWorkStatus);
    AcsWorkDTO getAcsWordData(int doorCount, int cardCount, int alarmCount);
}
