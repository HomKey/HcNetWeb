package com.keydak.hc.service;

import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.core.structure.AcsWorkStatus;
import com.keydak.hc.dto.AcsWorkDTO;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope("singleton")
public class HcDoorAccessService implements IHcDoorAccessService {
    private static final Logger logger = LogManager.getLogger(HcDoorAccessService.class);

    @Resource
    private HCNetSDK.FMSGCallBack fmsgCallBack;

    //报警监听句柄
    private Map<String, Integer> listenHandleMap = new HashMap<>();

    // 门禁状态 单例数据
    private AcsWorkDTO acsWorkDTO = new AcsWorkDTO();

    @PostConstruct
    public void init() {
    }

    @Override
    public void startAlarmListen(String deviceIp, short devicePort) {
        logger.info("startAlarmListen");
        Pointer pUser = null;
        int lListenHandle = HCNetSDK.INSTANCE.NET_DVR_StartListen_V30(deviceIp, devicePort, fmsgCallBack, pUser);
        listenHandleMap.put(deviceIp + ":" + devicePort, lListenHandle);
        if (lListenHandle < 0) {
            logger.error("启动监听失败，错误号:" + HCNetSDK.INSTANCE.NET_DVR_GetLastError());
        } else {
            logger.info("启动监听成功");
        }
    }

    @Override
    public void stopAlarmListen() {
        logger.info("stopAlarmListen");
        for (Map.Entry<String, Integer> handle : listenHandleMap.entrySet()) {
            if (handle.getValue() >= 0){
                boolean b = HCNetSDK.INSTANCE.NET_DVR_StopListen_V30(handle.getValue());
                if (b){
                    logger.info("撤防成功");
                    handle.setValue(-1);
                }
            }
        }
    }

    @Override
    public boolean updateAcsWorkData(int userId) {
        logger.info("updateAcsWorkData");
        AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50 acsWorkStatus = new AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50();
//        acsWorkStatus.clear();
        acsWorkStatus.write();
        Pointer acsWorkStatusPointer = acsWorkStatus.getPointer();
        IntByReference ibrBytesReturned = new IntByReference(0);
        boolean flag = HCNetSDK.INSTANCE.NET_DVR_GetDVRConfig(userId, AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50, 0xFFFFFFFF, acsWorkStatusPointer, acsWorkStatus.size(), ibrBytesReturned);
        acsWorkStatus.read();
        if (flag) {
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
        return flag;
    }

    @Override
    public AcsWorkDTO getAcsWordData(int doorCount, int cardCount, int alarmCount) {
        logger.info("getAcsWordData");
        AcsWorkDTO acsWorkDTO = new AcsWorkDTO();
        BeanUtils.copyProperties(this.acsWorkDTO, acsWorkDTO);
        doorCount = Math.min(doorCount, this.acsWorkDTO.getDoors().size());
        acsWorkDTO.setDoors(new ArrayList<>());
        for (int i = 0; i < doorCount; i++) {
            acsWorkDTO.addDoors(this.acsWorkDTO.getDoors().get(i));
        }
        cardCount = Math.min(cardCount, this.acsWorkDTO.getCardReaders().size());
        acsWorkDTO.setCardReaders(new ArrayList<>());
        for (int i = 0; i < cardCount; i++) {
            acsWorkDTO.addCardReaders(this.acsWorkDTO.getCardReaders().get(i));
        }
        alarmCount = Math.min(alarmCount, this.acsWorkDTO.getAlarmHosts().size());
        acsWorkDTO.setAlarmHosts(new ArrayList<>());
        for (int i = 0; i < alarmCount; i++) {
            acsWorkDTO.addAlarmHosts(this.acsWorkDTO.getAlarmHosts().get(i));
        }
        return acsWorkDTO;
    }

}
