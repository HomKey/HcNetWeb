package com.keydak.hc;

import com.keydak.hc.service.IEventInfoService;
import com.keydak.hc.service.IHcDoorAccessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Order(1)
public class HcDoorAccessApplication implements ApplicationRunner {
    private static final Logger logger = LogManager.getLogger(HcDoorAccessApplication.class);
    private boolean test = true;
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Autowired
    private IHcDoorAccessService hcDoorAccessService;

    @Autowired
    private IEventInfoService eventInfoService;

    @Value("${HcNet.deviceIp}")
    private String deviceIp;//已登录设备的IP地址
    @Value("${HcNet.port}")
    private short devicePort;//已登录设备的端口
    @Value("${HcNet.userName}")
    private String userName;//设备用户名
    @Value("${HcNet.password}")
    private String password;//设备密码

    private int lUserID = -1;//用户句柄
    private int lAlarmHandle = -1;//报警布防句柄
    private int lListenHandle = -1;//报警监听句柄

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!test) {
            if (hcDoorAccessService.login(deviceIp, devicePort, userName, password) == -1){
                logger.error("注册失败");
                return;
            };
        }
        //获取当前门状态
        getAndSaveAcsWorkStatus();
//        hcDoorAccessService.setupAlarmChan();
//        hcDoorAccessService.startAlarmListen();
    }

    /**
     * 获取当前门状态
     *
     * @param lUserID
     * @return
     */
    /**fixedDelay:上一次执行完毕时间点之后5秒再执行*/
    @Scheduled(fixedDelay = 5000)
    private void getAndSaveAcsWorkStatus() {
        if (test){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(df.format(new Date()));
            return;
        }
        hcDoorAccessService.updateAcsWorkData();
    }
}
