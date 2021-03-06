package com.keydak.hc.service;

import com.keydak.hc.config.HcNetDeviceConfig;
import com.keydak.hc.entity.DcimAlarmInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class DcimAlarmInfoServiceTest {

//    @Resource
//    private IDcimAlarmInfoService dcimAlarmInfoService;

//    @Resource
//    private HcNetDeviceConfig hcNetDeviceConfig;

    @Test
    void save() {

        Calendar calendar = Calendar.getInstance();
        int year = 2020;
        int month = 12;
        int day = 14;
        int hour = 11;
        int minute = 20;
        int second = 30;
        calendar.set(2020, 12-1, 14, 11, 20, 30);
        System.out.println(calendar.getTime());
    }
}