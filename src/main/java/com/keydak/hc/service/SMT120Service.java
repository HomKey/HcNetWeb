package com.keydak.hc.service;

import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * User: vk
 * Date: 2017/2/6
 * Time: 11:05
 * Description:
 */
@Service("smt120Service")
public class SMT120Service implements ISmtService{
    private static String WORD_FORMAT = "send_sms(\"%s\",\"%s\")";
    private static String VOICE_FORMAT = "rf_call(\"%s\",\"%s\")";
    private final String prompt = ">"; //结束标识字符串,Windows中是>,Linux中是#
    private final char promptChar = '>';   //结束标识字符

    @Override
    public void sendSMS(String host, int port, String number, String content){
        send(host,port,getSMTSmsMessage(number, content));
    }
    @Override
    public void sendVoice(String host, int port, String number, String content){
        send(host,port,getSMTVoiceMessage(number, content));
    }

    private void send(String host, int port, byte[] data) {
        TelnetClient telnetClient = new TelnetClient();
        telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
        OutputStream out = null;
        InputStream in = null;
        try {
            telnetClient.connect(host, port);  //建立一个连接,默认端口是23
            out = telnetClient.getOutputStream();  //写命令的流
            in = telnetClient.getInputStream();
            out.write(data); //写命令
            out.write('\r');
            out.write('\n');
            out.flush(); //将命令发送到telnet Server
            String result = readUntil(in, prompt);
            telnetClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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