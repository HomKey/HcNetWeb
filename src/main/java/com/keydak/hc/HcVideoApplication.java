package com.keydak.hc;

import com.keydak.hc.config.HcNetDeviceConfig;
import com.keydak.hc.core.HCNetSDK;
import com.keydak.hc.enums.HikSetUpAlarmEnum;
import com.keydak.hc.service.IHikService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;


@Component
@Order(1)
public class HcVideoApplication implements ApplicationRunner {
    private static final Logger logger = LogManager.getLogger(HcVideoApplication.class);
    private int[] userIds;
    private int[] alarmChanIds;

    @Resource
    private IHikService hikService;

    @Resource
    private HcNetDeviceConfig hcNetDeviceConfig;

    @Override
    public void run(ApplicationArguments args) {
        List<HcNetDeviceConfig.VideoInfo> videos = hcNetDeviceConfig.getVideos();
        if (videos == null) return;
        userIds = new int[videos.size()];
        alarmChanIds = new int[videos.size()];
        for (int i = 0; i < videos.size(); i++) {
            HcNetDeviceConfig.DeviceInfo video = videos.get(i);
            int userId = hikService.login(video.getDeviceIp(), Short.parseShort(video.getPort()), video.getUserName(), video.getPassword());
            logger.info(userId);
            if (userId == -1) {
                logger.info(video.getUserName() + ":" + video.getPassword());
                logger.error("注册失败，错误号:" + HCNetSDK.INSTANCE.NET_DVR_GetLastError());
            } else {
                logger.info("注册成功");
                userIds[i] = userId;
                alarmChanIds[i] = hikService.setUpAlarmChan(userIds[i], HikSetUpAlarmEnum.Level.MIDDLE, HikSetUpAlarmEnum.AlarmInfoType.NEW,HikSetUpAlarmEnum.DeployType.REALTIME);
            }
        }
    }
    @PreDestroy
    public void destroy(){
        if (alarmChanIds != null){
            for (int alarmChanId : alarmChanIds) {
                hikService.closeAlarmChan(alarmChanId);
            }
        }
        if (userIds != null){
            for (int userId : userIds) {
                hikService.logout(userId);
            }
        }
    }
}
