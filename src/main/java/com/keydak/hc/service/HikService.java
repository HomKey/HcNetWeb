package com.keydak.hc.service;

import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.enums.HikSetUpAlarmEnum;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Service("hikService")
@Scope("singleton")
public class HikService implements IHikService {
    private static final Logger logger = LogManager.getLogger(HikService.class);
    private HCNetSDK hCNetSDK;
    private HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo; //设备登录信息
    private HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo; // new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息

    @Resource
    private HCNetSDK.FMSGCallBack_V31 fmsgCallBackV31;

    @PostConstruct
    public void init() {
        hCNetSDK = HCNetSDK.INSTANCE;
        if (!hCNetSDK.NET_DVR_Init()) {
            logger.error("NET_DVR_Init error");
        }
        Pointer pUser = null;
        if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fmsgCallBackV31, pUser)) {
            logger.error("NET_DVR_SetDVRMessageCallBack_V31 error");
        }
        m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
    }

    @PreDestroy
    private void destroy() {
        if (!hCNetSDK.NET_DVR_Cleanup()) {
            logger.error("NET_DVR_Cleanup error");
        }
    }

    @Override
    public int login(String m_sDeviceIP, short m_sPort, String m_sUsername, String m_sPassword) {
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        m_strLoginInfo.wPort = m_sPort;
        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
        m_strLoginInfo.write();
        return hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
    }

    @Override
    public boolean logout(int userId) {
        if (userId < 0) return false;
        return hCNetSDK.NET_DVR_Logout(userId);
    }


    @Override
    public int setUpAlarmChan(
            int userId,
            HikSetUpAlarmEnum.Level level,
            HikSetUpAlarmEnum.AlarmInfoType alarmInfoType,
            HikSetUpAlarmEnum.DeployType deployType
    ) {
        HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
        m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
        m_strAlarmInfo.byLevel = level.getValue();
        m_strAlarmInfo.byAlarmInfoType = alarmInfoType.getValue();
        m_strAlarmInfo.byDeployType = deployType.getValue();
        m_strAlarmInfo.write();
        return hCNetSDK.NET_DVR_SetupAlarmChan_V41(userId, m_strAlarmInfo);
    }

    @Override
    public boolean closeAlarmChan(int alarmChanId) {
        if (alarmChanId < 0) return false;
        return hCNetSDK.NET_DVR_CloseAlarmChan_V30(alarmChanId);
    }
}
