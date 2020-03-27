package com.keydak.hc.callback;

import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.entity.EventInfo;
import com.keydak.hc.service.IEventInfoService;
import com.keydak.hc.util.HexUtil;
import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MyFMSGCallBackV31 implements HCNetSDK.FMSGCallBack_V31 {

    @Autowired
    private IEventInfoService eventInfoService;

    private static ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512), // 使用有界队列，避免OOM
            new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        switch (lCommand) {
            case HCNetSDK.COMM_ALARM_ACS:
                HCNetSDK.NET_DVR_ACS_ALARM_INFO strACSInfo = new HCNetSDK.NET_DVR_ACS_ALARM_INFO();
                strACSInfo.write();
                Pointer pACSInfo = strACSInfo.getPointer();
                pACSInfo.write(0L, pAlarmInfo.getByteArray(0L, strACSInfo.size()), 0, strACSInfo.size());
                strACSInfo.read();
                executorService.execute(() -> {
                    EventInfo eventInfo = new EventInfo();
                    eventInfo.setMajorType(strACSInfo.dwMajor);
                    eventInfo.setMajorType(strACSInfo.dwMinor);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(strACSInfo.struTime.dwYear, strACSInfo.struTime.dwMonth, strACSInfo.struTime.dwDay, strACSInfo.struTime.dwHour, strACSInfo.struTime.dwMinute, strACSInfo.struTime.dwSecond);
                    eventInfo.setAlarmTime(calendar.getTime());
                    eventInfo.setRemoteHostAdd(
                            String.format("%s.%s.%s.%s",
                                    HexUtil.byteArrayToInt(strACSInfo.struRemoteHostAddr.sIpV4, 0, 4),
                                    HexUtil.byteArrayToInt(strACSInfo.struRemoteHostAddr.sIpV4, 3, 4),
                                    HexUtil.byteArrayToInt(strACSInfo.struRemoteHostAddr.sIpV4, 7, 4),
                                    HexUtil.byteArrayToInt(strACSInfo.struRemoteHostAddr.sIpV4, 11, 4))
                    );
                    eventInfo.setNetUser(new String(strACSInfo.sNetUser));
//                        eventInfo.setNetUser(Base64.getEncoder().encodeToString(strACSInfo.sNetUser));
                    eventInfo.setChannelNo(strACSInfo.dwIOTChannelNo);
                    eventInfoService.save(eventInfo);
                });
                break;
//            case HCNetSDK.COMM_ID_INFO_ALARM:
//                break;
//            case HCNetSDK.COMM_PASSNUM_INFO_ALARM:
//                break;
            default:
                break;
        }
        return true;
    }
}
