package com.keydak.hc.service;

import com.keydak.hc.callback.MyFMSGCallBackV31;
import com.keydak.hc.config.SmsConfig;
import com.keydak.hc.dto.SmsData;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: vk
 * Date: 2017/2/6
 * Time: 11:05
 * Description:
 */
@Service("smt120Service")
public class SMT120Service implements ISmtService {
    private static final Logger logger = LogManager.getLogger(SMT120Service.class);
    private static String WORD_FORMAT = "send_sms(\"%s\",\"%s\")";
    private static String VOICE_FORMAT = "rf_call(\"%s\",\"%s\")";
    private final String prompt = ">"; //结束标识字符串,Windows中是>,Linux中是#
    private final char promptChar = '>';   //结束标识字符
    private TelnetClient telnetClient;

    public static Queue<SmsData> data = new LinkedList<>();

    @Resource
    private SmsConfig smsConfig;



    @Value("${holiday}")
    private Boolean holiday;

    @PostConstruct
    public void connect() {
//        logger.info("connect");
//        if (telnetClient == null){
//            telnetClient = new TelnetClient();
//            telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
//        }
//        try{
//            telnetClient.connect(smsConfig.getHost(), Integer.parseInt(smsConfig.getPort()));  //建立一个连接,默认端口是23
//            Thread.sleep(2000);
//        } catch (Exception e){
//            e.printStackTrace();
//            logger.info("connect exception");
//        }
//        logger.info("connect end");
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduled2() {
        if (!data.isEmpty()) {
            SmsData item = data.poll();
            sendSMS(item.getNumber(), item.getContent());
        }
    }
//    @PreDestroy
//    public void telNetDestroy(){
//        if (telnetClient != null){
//            try {
//                telnetClient.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public boolean sendSMS(String number, String content) {
        logger.info("sendSMS:" + number + "(" + content + ")");
        return send(getSMTSmsMessage(number, content));
    }

    @Override
    public boolean sendVoice(String number, String content) {
        logger.info("sendVoice:" + number + "(" + content + ")");
        return send(getSMTVoiceMessage(number, content));
    }

    private boolean send(byte[] data) {
        AtomicBoolean result = new AtomicBoolean(false);
        logger.info("send");
//        int count = 5;
//        boolean isConnect;
//        do{
//            isConnect = telnetClient.isConnected();
//            logger.info("isConnect:" + isConnect);
//            if (!isConnect){
//                connect();
//            }
//            count--;
//        }while (!isConnect && count >= 0);
//        if (!isConnect) {
//            return result.get();
//        }
        OutputStream out = null;
        InputStream in = null;
        TelnetClient telnetClient = null;
        try {
            telnetClient = new TelnetClient();
            telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
            telnetClient.connect(smsConfig.getHost(), Integer.parseInt(smsConfig.getPort()));  //建立一个连接,默认端口是23
            Thread.sleep(2000);
//            int retry = 3;
//            boolean isSuccess;
//            do {
//                isSuccess = true;
                out = telnetClient.getOutputStream();  //写命令的流
                in = telnetClient.getInputStream();
                out.write(data); //写命令
                out.write('\r');
                out.write('\n');
                out.flush(); //将命令发送到telnet Server
                String receiveStr = readUntil(in, prompt);
                logger.info(receiveStr);
//                Thread.sleep(5000);
//            } while (!isSuccess && retry > 0);
            result.set(true);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            result.set(false);
        } finally {
            if (telnetClient != null) {
                try {
                    telnetClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.get();
    }

    /**
     * 读取分析结果
     *
     * @param pattern 匹配到该字符串时返回结果
     * @return
     */
    private String readUntil(InputStream in, String pattern) {
        StringBuffer sb = new StringBuffer();
        try {
            char lastChar = (char) -1;
            boolean flag = pattern != null && pattern.length() > 0;
            if (flag)
                lastChar = pattern.charAt(pattern.length() - 1);
            char ch;
            int code = -1;
            while ((code = in.read()) != -1) {
                ch = (char) code;
                sb.append(ch);
                //匹配到结束标识时返回结果
                if (flag) {
                    if (ch == lastChar && sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                } else {
                    //如果没指定结束标识,匹配到默认结束标识字符时返回结果
                    if (ch == promptChar)
                        return sb.toString();
                }
                //登录失败时返回结果
                if (sb.toString().contains("Login Failed")) {
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            System.out.println("abssss");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private byte[] getSMTSmsMessage(String number, String content) {
        try {
            return (String.format(WORD_FORMAT, number, content) + "\r\n").getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    private byte[] getSMTVoiceMessage(String number, String content) {
        try {
            return (String.format(VOICE_FORMAT, number, content) + "\r\n").getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }
}