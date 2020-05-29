package com.keydak.hc;

import com.keydak.hc.config.HcNetDeviceConfig;
import com.keydak.hc.enums.HikSetUpAlarmEnum;
import com.keydak.hc.service.IEventInfoService;
import com.keydak.hc.service.IHcDoorAccessService;
import com.keydak.hc.service.IHikService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;


@Component
@Order(1)
public class HcDoorAccessApplication implements ApplicationRunner {
    private static final Logger logger = LogManager.getLogger(HcDoorAccessApplication.class);
    private int[] userIds;
    private int[] alarmChanIds;

    @Resource
    private IHcDoorAccessService hcDoorAccessService;

    @Resource
    private IHikService hikService;

    @Resource
    private HcNetDeviceConfig hcNetDeviceConfig;

    @Override
    public void run(ApplicationArguments args) {
        List<HcNetDeviceConfig.DeviceInfo> doors = hcNetDeviceConfig.getDoors();
        if (doors == null) return;
        userIds = new int[doors.size()];
        alarmChanIds = new int[doors.size()];
        for (int i = 0; i < doors.size(); i++) {
            HcNetDeviceConfig.DeviceInfo door = doors.get(i);
            int userId = hikService.login(door.getDeviceIp(), Short.parseShort(door.getPort()), door.getUserName(), door.getPassword());
            userIds[i] = userId;
            alarmChanIds[i] = -1;
            if (userId == -1) {
                logger.error("注册失败");
            } else {
                alarmChanIds[i] = hikService.setUpAlarmChan(userIds[i], HikSetUpAlarmEnum.Level.MIDDLE, HikSetUpAlarmEnum.AlarmInfoType.NEW,HikSetUpAlarmEnum.DeployType.REALTIME);
                hcDoorAccessService.startAlarmListen(door.getDeviceIp(), Short.parseShort(door.getPort()));
            }
        }
    }

    /**
     * 获取当前门状态
     *
     * @param lUserID
     * @return
     */
    /**
     * fixedDelay:上一次执行完毕时间点之后5秒再执行
     */
    @Scheduled(fixedDelay = 5000)
    private void getAndSaveAcsWorkStatus() {
        for (int userId : userIds) {
            if (userId >= 0) hcDoorAccessService.updateAcsWorkData(userId);
        }
    }

    @PreDestroy
    public void destroy(){
        hcDoorAccessService.stopAlarmListen();
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
