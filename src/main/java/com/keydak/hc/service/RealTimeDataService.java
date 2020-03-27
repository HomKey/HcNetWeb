package com.keydak.hc.service;

import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.core.structure.AcsWorkStatus;
import com.keydak.hc.dto.AcsWorkDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class RealTimeDataService implements IRealTimeDataService {

    private AcsWorkDTO acsWorkDTO;

    @Override
    public void saveAcsWorkData(AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50 acsWorkStatus) {
        Boolean[] caseStatus = new Boolean[8];
        for (int i = 0; i < acsWorkStatus.byCaseStatus.length; i++) {
            caseStatus[i] = acsWorkStatus.byCaseStatus[i] > 0;
        }
        this.acsWorkDTO.setCaseStatus(caseStatus);
        this.acsWorkDTO.setBatteryVoltage(acsWorkStatus.wBatteryVoltage);
        this.acsWorkDTO.setBatteryLowVoltage(acsWorkStatus.byBatteryLowVoltage > 0);
        this.acsWorkDTO.setPowerSupplyStatus(acsWorkStatus.byPowerSupplyStatus);
        this.acsWorkDTO.setMultiDoorInterlockStatus(acsWorkStatus.byMultiDoorInterlockStatus > 0); // 多门互锁状态：0- 关闭，1- 开启
        this.acsWorkDTO.setAntiSneakStatus(acsWorkStatus.byAntiSneakStatus > 0); // 反潜回状态：0-关闭，1-开启
        this.acsWorkDTO.setHostAntiDismantleStatus(acsWorkStatus.byHostAntiDismantleStatus > 0); // 主机防拆状态：0- 关闭，1- 开启
        this.acsWorkDTO.setIndicatorLightStatus(acsWorkStatus.byIndicatorLightStatus > 0); // 指示灯状态，0-掉线，1-在线
        for (int i = 0; i < HCNetSDK.MAX_DOOR_NUM_256; i++) {
            AcsWorkDTO.Door door = new AcsWorkDTO.Door();
            door.setLockStatus(acsWorkStatus.byDoorLockStatus[i]);
            door.setStatus(acsWorkStatus.byDoorStatus[i]);
            door.setMagneticStatus(acsWorkStatus.byMagneticStatus[i]);
            this.acsWorkDTO.addDoors(door);
        }
        for (int i = 0; i < HCNetSDK.MAX_CARD_READER_NUM_512; i++) {
            AcsWorkDTO.CardReader cardReader = new AcsWorkDTO.CardReader();
            cardReader.setCardReaderOnlineStatus(acsWorkStatus.byCardReaderOnlineStatus[i] > 0);
            cardReader.setCardReaderAntiDismantleStatus(acsWorkStatus.byCardReaderAntiDismantleStatus[i] > 0);
            cardReader.setCardReaderVerifyMode(acsWorkStatus.byCardReaderVerifyMode[i]);
            this.acsWorkDTO.addCardReaders(cardReader);
        }
        for (int i = 0; i < HCNetSDK.MAX_ALARMHOST_ALARMIN_NUM; i++) {
            AcsWorkDTO.AlarmHost alarmHost = new AcsWorkDTO.AlarmHost();
            alarmHost.setSetupAlarmStatus(acsWorkStatus.bySetupAlarmStatus[i] > 0);
            alarmHost.setAlarmInStatus(acsWorkStatus.byAlarmInStatus[i] > 0);
            alarmHost.setAlarmOutStatus(acsWorkStatus.byAlarmOutStatus[i] > 0);
            this.acsWorkDTO.addAlarmHosts(alarmHost);
        }
        this.acsWorkDTO.setDwCardNum(acsWorkStatus.dwCardNum);
        this.acsWorkDTO.setFireAlarmStatus(acsWorkStatus.byFireAlarmStatus);
        this.acsWorkDTO.setBatteryChargeStatus(acsWorkStatus.byBatteryChargeStatus);
        this.acsWorkDTO.setMasterChannelControllerStatus(acsWorkStatus.byMasterChannelControllerStatus);
        this.acsWorkDTO.setSlaveChannelControllerStatus(acsWorkStatus.bySlaveChannelControllerStatus);
        this.acsWorkDTO.setAntiSneakServerStatus(acsWorkStatus.byAntiSneakServerStatus);
        this.acsWorkDTO.setDwWhiteFaceNum(acsWorkStatus.dwWhiteFaceNum);
        this.acsWorkDTO.setDwBlackFaceNum(acsWorkStatus.dwBlackFaceNum);
    }

    @Override
    public AcsWorkDTO getAcsWordData(int doorCount, int cardCount, int alarmCount) {
        AcsWorkDTO acsWorkDTO = new AcsWorkDTO();
        BeanUtils.copyProperties(acsWorkDTO, this.acsWorkDTO);
        doorCount = Math.min(doorCount, this.acsWorkDTO.getDoors().size());
        for (int i = 0; i < doorCount; i++) {
            acsWorkDTO.addDoors(this.acsWorkDTO.getDoors().get(i));
        }
        cardCount = Math.min(cardCount, this.acsWorkDTO.getCardReaders().size());
        for (int i = 0; i < cardCount; i++) {
            acsWorkDTO.addCardReaders(this.acsWorkDTO.getCardReaders().get(i));
        }
        alarmCount = Math.min(alarmCount, this.acsWorkDTO.getAlarmHosts().size());
        for (int i = 0; i < alarmCount; i++) {
            acsWorkDTO.addAlarmHosts(this.acsWorkDTO.getAlarmHosts().get(i));
        }
        return acsWorkDTO;
    }
}
