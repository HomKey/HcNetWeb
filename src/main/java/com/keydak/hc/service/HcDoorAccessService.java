package com.keydak.hc.service;

import com.keydak.hc.callback.MyFMSGCallBack;
import com.keydak.hc.callback.MyFMSGCallBackV31;
import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.core.HCNetSDKPath;
import com.keydak.hc.core.structure.AcsWorkStatus;
import com.keydak.hc.dto.AcsWorkDTO;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;

@Service
@Scope("singleton")
public class HcDoorAccessService implements IHcDoorAccessService {
    private static final Logger logger = LogManager.getLogger(HcDoorAccessService.class);

    private HCNetSDK hCNetSDK;
    private HCNetSDK.FMSGCallBack_V31 fMSFCallBack_V31; // 布防
    private HCNetSDK.FMSGCallBack fMSFCallBack;
    private AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50 acsWorkStatus;
    private HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo; //设备登录信息
    private HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo; // new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息

    // product
//    private HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
//    private HCNetSDK.FMSGCallBack_V31 fMSFCallBack_V31 = new MyFMSGCallBackV31();
//    private HCNetSDK.FMSGCallBack fMSFCallBack = new MyFMSGCallBack();
//    private AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50 acsWorkStatus = new AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50();
//    private HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
//    private HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息

    private int lUserID = -1;//用户句柄
    private int lAlarmHandle = -1;//报警布防句柄
    private int lListenHandle = -1;//报警监听句柄

    private AcsWorkDTO acsWorkDTO = new AcsWorkDTO();

    @PostConstruct
    public void init() {
        logger.info("init");
        hCNetSDK = HCNetSDK.INSTANCE;
        fMSFCallBack_V31 = new MyFMSGCallBackV31();
        fMSFCallBack = new MyFMSGCallBack();
        acsWorkStatus = new AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50();
        m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
    }

    @Override
    public int login(String m_sDeviceIP, short m_sPort, String m_sUsername, String m_sPassword) {
        logger.info("login");
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        m_strLoginInfo.wPort = m_sPort;
        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
        m_strLoginInfo.write();
        lUserID = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        return lUserID;
    }

    @Override
    @PreDestroy
    public void logout() {
        logger.info("logout");
        closeAlarmChan();
        stopAlarmListen();
        if (lUserID > -1) {
            hCNetSDK.NET_DVR_Logout(lUserID);
            hCNetSDK.NET_DVR_Cleanup();
            lUserID = -1;
        }
    }

    @Override
    public void setupAlarmChan() {
        logger.info("setupAlarmChan");
        //设置报警回调函数，这个函数将会上次人脸识别比对结果
        Pointer pUser = null;
        if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
            logger.error("设置回调函数失败!");
        }
        HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
        m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
        m_strAlarmInfo.byLevel = 1;//智能交通布防优先级：0- 一等级（高），1- 二等级（中），2- 三等级（低）
        m_strAlarmInfo.byAlarmInfoType = 1;//智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
        m_strAlarmInfo.byDeployType = 1; //布防类型(仅针对门禁主机、人证设备)：0-客户端布防(会断网续传)，1-实时布防(只上传实时数据)
        m_strAlarmInfo.write();
        lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);
        if (lAlarmHandle == -1) {
            logger.error("布防失败，错误号:" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            logger.info("布防成功");
        }
    }

    @Override
    public void closeAlarmChan() {
        logger.info("closeAlarmChan");
        if (lAlarmHandle > -1) {
            if (hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle)) {
                logger.info("撤防成功");
                lAlarmHandle = -1;
            }
        }
    }

    @Override
    public void startAlarmListen(String deviceIp, short devicePort) {
        logger.info("startAlarmListen");
        Pointer pUser = null;
        lListenHandle = hCNetSDK.NET_DVR_StartListen_V30(deviceIp, devicePort, fMSFCallBack, pUser);
        if (lListenHandle < 0) {
            logger.error("启动监听失败，错误号:" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            logger.info("启动监听成功");
        }
    }

    @Override
    public void stopAlarmListen() {
        logger.info("stopAlarmListen");
        if (lListenHandle > -1) {
            if (hCNetSDK.NET_DVR_StopListen_V30(lListenHandle)) {
                logger.info("撤防成功");
                lListenHandle = -1;
            }
        }
    }

    @Override
    public boolean updateAcsWorkData() {
        logger.info("updateAcsWorkData");
        acsWorkStatus.clear();
        acsWorkStatus.write();
        Pointer acsWorkStatusPointer = acsWorkStatus.getPointer();
        acsWorkStatus.read();
        IntByReference ibrBytesReturned = new IntByReference(0);
        boolean flag = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50, 0xFFFFFFFF, acsWorkStatusPointer, acsWorkStatus.size(), ibrBytesReturned);
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
