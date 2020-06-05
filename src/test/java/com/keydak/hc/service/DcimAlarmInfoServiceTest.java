package com.keydak.hc.service;

import com.keydak.hc.config.HcNetDeviceConfig;
import com.keydak.hc.entity.DcimAlarmInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DcimAlarmInfoServiceTest {

    @Resource
    private IDcimAlarmInfoService dcimAlarmInfoService;

    @Resource
    private HcNetDeviceConfig hcNetDeviceConfig;

    @Test
    void save() {
        List<HcNetDeviceConfig.VideoInfo> videos = hcNetDeviceConfig.getVideos();
        for (HcNetDeviceConfig.VideoInfo videoInfo :
                videos) {
            DcimAlarmInfo dcimAlarmInfo = new DcimAlarmInfo();
            dcimAlarmInfo.setId(UUID.randomUUID().toString());
            dcimAlarmInfo.setDeviceId(videoInfo.getDeviceIp());
            dcimAlarmInfo.setDeviceName(videoInfo.getName());
            dcimAlarmInfo.setAlarmTime(new Date());
            dcimAlarmInfo.setDetailStore("");
            dcimAlarmInfo.setAlarmInfo(videoInfo.getAlarmInfo());
            dcimAlarmInfoService.save(dcimAlarmInfo);
        }
    }
}