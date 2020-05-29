package com.keydak.hc.callback;

import com.keydak.hc.core.HCNetSDK;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Component;

@Component("fmsgCallBack")
public class MyFMSGCallBack implements HCNetSDK.FMSGCallBack {
    @Override
    public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {

    }
}
