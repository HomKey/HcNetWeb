package com.keydak.hc;

import com.keydak.hc.callback.MyFMSGCallBack;
import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.core.structure.AcsWorkStatus;
import com.keydak.hc.callback.MyFMSGCallBackV31;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Order(1)
public class HcDoorAccessApplication implements ApplicationRunner {
    private static final Logger logger = LogManager.getLogger(HcDoorAccessApplication.class);
    static HCNetSDK hCNetSDK = null;// HCNetSDK.INSTANCE;
    HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = null; // new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
    HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = null; // new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
    String deviceIp;//已登录设备的IP地址
    short devicePort;//已登录设备的端口
    String userName;//设备用户名
    String password;//设备密码

    private static int lUserID = -1;//用户句柄
    int lAlarmHandle = -1;//报警布防句柄
    int lListenHandle = -1;//报警监听句柄

    // 布防
    @Autowired
    private HCNetSDK.FMSGCallBack_V31 fMSFCallBack_V31;
    @Autowired
    private HCNetSDK.FMSGCallBack fMSFCallBack;

    AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50 acsWorkStatus;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initParamConfig();
//        login(deviceIp, devicePort, userName, password);
        if (lUserID == -1) {
            logger.error("注册失败");
            return;
        }
        //获取当前门状态
        if (getAcsWorkStatus(lUserID)) {
            // acsWorkStatus
        }
        setupAlarmChan();
//        startAlarmListen();
    }

    private void initParamConfig() {
        deviceIp = "192.168.7.140";
        devicePort = 6001;
        userName = "user";
        password = "password";
//        fMSFCallBack_V31 = new MyFMSGCallBackV31();
//        fMSFCallBack = new MyFMSGCallBack();
//        acsWorkStatus = new AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50();
    }

    /**
     * 获取当前门状态
     *
     * @param lUserID
     * @return
     */
    private boolean getAcsWorkStatus(int lUserID) {
        // 获取当门状态
        acsWorkStatus.write();
        Pointer acsWorkStatusPointer = acsWorkStatus.getPointer();
        acsWorkStatus.read();
        IntByReference ibrBytesReturned = new IntByReference(0);
        return hCNetSDK.NET_DVR_GetDVRConfig(lUserID, AcsWorkStatus.NET_DVR_ACS_WORK_STATUS_V50, 0xFFFFFFFF, acsWorkStatusPointer, acsWorkStatus.size(), ibrBytesReturned);
    }

    /**
     * 设置布防
     */
    private void setupAlarmChan() {
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
    private void closeAlarmChan() {
        if (lAlarmHandle > -1) {
            if (hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle)) {
                logger.info("撤防成功");
                lAlarmHandle = -1;
            }
        }
    }

    private void startAlarmListen() {
        Pointer pUser = null;
        lListenHandle = hCNetSDK.NET_DVR_StartListen_V30(deviceIp, devicePort, fMSFCallBack, pUser);
        if (lListenHandle < 0) {
            logger.error("启动监听失败，错误号:" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            logger.info("启动监听成功");
        }
    }
    private void stopAlarmListen(){
        if (lAlarmHandle > -1) {
            if (hCNetSDK.NET_DVR_StopListen_V30(lAlarmHandle)) {
                logger.info("撤防成功");
                lAlarmHandle = -1;
            }
        }
    }

    private void login(String m_sDeviceIP, short m_sPort, String m_sUsername, String m_sPassword) {
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
    }

    private void logout() {
        closeAlarmChan();
        stopAlarmListen();
        if (lUserID > -1) {
            hCNetSDK.NET_DVR_Logout(lUserID);
            hCNetSDK.NET_DVR_Cleanup();
            lUserID = -1;
        }
    }

}
