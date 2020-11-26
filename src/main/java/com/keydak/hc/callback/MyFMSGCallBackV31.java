package com.keydak.hc.callback;

import com.keydak.hc.HcVideoApplication;
import com.keydak.hc.config.HcNetDeviceConfig;
import com.keydak.hc.config.SmsConfig;
import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.dto.SmsData;
import com.keydak.hc.entity.DcimAlarmInfo;
import com.keydak.hc.entity.EventInfo;
import com.keydak.hc.enums.HikVcaEventEnum;
import com.keydak.hc.service.IDcimAlarmInfoService;
import com.keydak.hc.service.IEventInfoService;
import com.keydak.hc.service.ISmtService;
import com.keydak.hc.service.SMT120Service;
import com.keydak.hc.util.HexUtil;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

//@Component("fmsgCallBackV31")
public class MyFMSGCallBackV31 implements HCNetSDK.FMSGCallBack_V31 {
    @Resource
    private IEventInfoService eventInfoService;
    @Resource
    private IDcimAlarmInfoService dcimAlarmInfoService;
    @Resource
    private HcNetDeviceConfig hcNetDeviceConfig;

    @Value("${holiday}")
    private Boolean holiday;

    @Resource
    private SmsConfig smsConfig;
    @Resource
    private ISmtService smt120Service;

    private static ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512), // 使用有界队列，避免OOM
            new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        System.out.println(lCommand);
        switch (lCommand) {
            case HCNetSDK.COMM_ALARM_RULE:
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.DAY_OF_WEEK);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                System.out.println(week + ":" + hour + ":" + minute);
                if (holiday || week == 1 || week == 7 || (hour < 8
                        || (hour >= 12 && (hour <= 14 && minute < 30))
                        || (hour >= 17 && minute > 30))) {
                    HCNetSDK.NET_VCA_RULE_ALARM alarmInfo = new HCNetSDK.NET_VCA_RULE_ALARM();
                    alarmInfo.write();
                    Pointer pInfoV40 = alarmInfo.getPointer();
                    pInfoV40.write(0, pAlarmInfo.getByteArray(0, alarmInfo.size()), 0, alarmInfo.size());
                    alarmInfo.read();
                    int dwEventType = alarmInfo.struRuleInfo.wEventTypeEx;
                    if (HikVcaEventEnum.INTRUSION == HikVcaEventEnum.get(dwEventType)) {
                        byte[] sIpV4 = alarmInfo.struDevInfo.struDevIP.sIpV4;
                        String deviceIp = new String(sIpV4, StandardCharsets.UTF_8).trim();
                        short wPort = alarmInfo.struDevInfo.wPort;
                        HcNetDeviceConfig.VideoInfo videoInfo = hcNetDeviceConfig.getVideos(deviceIp, String.valueOf(wPort));
                        if (videoInfo == null) {
                            videoInfo = new HcNetDeviceConfig.VideoInfo();
                            videoInfo.setDeviceIp("未知");
                            videoInfo.setName("未知");
                            videoInfo.setAlarmInfo(new HcNetDeviceConfig.AlarmInfo());
                        }
                        // database
                        DcimAlarmInfo dcimAlarmInfo = new DcimAlarmInfo();
                        dcimAlarmInfo.setId(UUID.randomUUID().toString());
                        dcimAlarmInfo.setEventId(0);
                        dcimAlarmInfo.setDeviceId(videoInfo.getDeviceId());
                        dcimAlarmInfo.setDeviceName(videoInfo.getName());
                        dcimAlarmInfo.setDetailStore("");
                        dcimAlarmInfo.setAlarmTime(new Date());
                        dcimAlarmInfo.setAlarmInfo(videoInfo.getAlarmInfo());
                        System.out.println("save alarmInfo");
                        dcimAlarmInfoService.save(dcimAlarmInfo);
                        // sms
                        for (String number : smsConfig.getNumberArray()) {
                            SMT120Service.data.add(new SmsData(number, dcimAlarmInfo.getAlarmContent()));
                        }
                    }
                }
                break;
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
//                    NET_DVR_ACS_EVENT_INFO
                    eventInfo.setCardNo(new String(strACSInfo.struAcsEventInfo.byCardNo));
                    eventInfo.setCardType(strACSInfo.struAcsEventInfo.byCardType);
                    eventInfo.setWhiteListNo(strACSInfo.struAcsEventInfo.byWhiteListNo);
                    eventInfo.setReportChannel(strACSInfo.struAcsEventInfo.byReportChannel);
                    eventInfo.setCardReaderKind(strACSInfo.struAcsEventInfo.byCardReaderKind);
                    eventInfo.setCardReaderNo(strACSInfo.struAcsEventInfo.dwCardReaderNo);
                    eventInfo.setDoorNo(strACSInfo.struAcsEventInfo.dwDoorNo);
                    eventInfo.setVerifyNo(strACSInfo.struAcsEventInfo.dwVerifyNo);
                    eventInfo.setAlarmInNo(strACSInfo.struAcsEventInfo.dwAlarmInNo);
                    eventInfo.setAlarmOutNo(strACSInfo.struAcsEventInfo.dwAlarmOutNo);
                    eventInfo.setCaseSensorNo(strACSInfo.struAcsEventInfo.dwCaseSensorNo);
                    eventInfo.setRs485No(strACSInfo.struAcsEventInfo.dwRs485No);
                    eventInfo.setMultiCardGroupNo(strACSInfo.struAcsEventInfo.dwMultiCardGroupNo);
                    eventInfo.setAccessChannel(strACSInfo.struAcsEventInfo.wAccessChannel);
                    eventInfo.setDeviceNo(strACSInfo.struAcsEventInfo.byDeviceNo);
                    eventInfo.setDistractControlNo(strACSInfo.struAcsEventInfo.byDistractControlNo);
                    eventInfo.setEmployeeNo(strACSInfo.struAcsEventInfo.dwEmployeeNo);
                    eventInfo.setLocalControllerID(strACSInfo.struAcsEventInfo.wLocalControllerID);

                    // ...
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

    @Override
    protected void finalize() throws Throwable {
        System.out.println("callbackv31 finalize");
        super.finalize();
    }
}
