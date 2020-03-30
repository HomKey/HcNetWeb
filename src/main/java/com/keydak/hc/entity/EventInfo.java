package com.keydak.hc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "hc_net_event_info")
public class EventInfo extends BaseEntity {

    private int majorType; // 报警主类型
    private int minorType; // 报警次类型
    private Date alarmTime;
    private String netUser; // 网络操作的用户名
    private int channelNo; // 通道编号
    private String remoteHostAdd; // 远程主机地址
    private String cardNo; //卡号
    private int cardType; // 卡类型：1- 普通卡，2- 残疾人卡，3- 黑名单卡，4- 巡更卡，5- 胁迫卡，6- 超级卡，7- 来宾卡，8- 解除卡，为0表示无效
    private int whiteListNo; // 白名单单号，取值范围：1~8，0表示无效
    private int reportChannel; // 报告上传通道：1- 布防上传，2- 中心组1上传，3- 中心组2上传，0表示无效
    private int cardReaderKind; // 读卡器类型：0- 无效，1- IC读卡器，2- 身份证读卡器，3- 二维码读卡器，4- 指纹头
    private int cardReaderNo; // 读卡器编号，为0表示无效
    private int doorNo; // 门编号（或者梯控的楼层编号），为0表示无效（当接的设备为人员通道设备时，门1为进方向，门2为出方向）
    private int verifyNo; // 多重卡认证序号， 为0表示无效
    private int alarmInNo;// 报警输入号，为0表示无效
    private int alarmOutNo;// 报警输出号，为0表示无效
    private int caseSensorNo;// 事件触发器编号
    private int rs485No;// RS485通道号，为0表示无效
    private int multiCardGroupNo;// 群组编号
    private int accessChannel; // 人员通道号
    private int deviceNo; // 设备编号，为0表示无效
    private int distractControlNo; // 分控器编号，为0表示无效
    private int employeeNo;// 工号，为0无效
    private int localControllerID; // 就地控制器编号，0-门禁主机，1-255代表就地控制器

    public int getMajorType() {
        return majorType;
    }

    public void setMajorType(int majorType) {
        this.majorType = majorType;
    }

    public int getMinorType() {
        return minorType;
    }

    public void setMinorType(int minorType) {
        this.minorType = minorType;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getNetUser() {
        return netUser;
    }

    public void setNetUser(String netUser) {
        this.netUser = netUser;
    }


    public int getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public String getRemoteHostAdd() {
        return remoteHostAdd;
    }

    public void setRemoteHostAdd(String remoteHostAdd) {
        this.remoteHostAdd = remoteHostAdd;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getWhiteListNo() {
        return whiteListNo;
    }

    public void setWhiteListNo(int whiteListNo) {
        this.whiteListNo = whiteListNo;
    }

    public int getReportChannel() {
        return reportChannel;
    }

    public void setReportChannel(int reportChannel) {
        this.reportChannel = reportChannel;
    }

    public int getCardReaderKind() {
        return cardReaderKind;
    }

    public void setCardReaderKind(int cardReaderKind) {
        this.cardReaderKind = cardReaderKind;
    }

    public int getCardReaderNo() {
        return cardReaderNo;
    }

    public void setCardReaderNo(int cardReaderNo) {
        this.cardReaderNo = cardReaderNo;
    }

    public int getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(int doorNo) {
        this.doorNo = doorNo;
    }

    public int getVerifyNo() {
        return verifyNo;
    }

    public void setVerifyNo(int verifyNo) {
        this.verifyNo = verifyNo;
    }

    public int getAlarmInNo() {
        return alarmInNo;
    }

    public void setAlarmInNo(int alarmInNo) {
        this.alarmInNo = alarmInNo;
    }

    public int getAlarmOutNo() {
        return alarmOutNo;
    }

    public void setAlarmOutNo(int alarmOutNo) {
        this.alarmOutNo = alarmOutNo;
    }

    public int getCaseSensorNo() {
        return caseSensorNo;
    }

    public void setCaseSensorNo(int caseSensorNo) {
        this.caseSensorNo = caseSensorNo;
    }

    public int getRs485No() {
        return rs485No;
    }

    public void setRs485No(int rs485No) {
        this.rs485No = rs485No;
    }

    public int getMultiCardGroupNo() {
        return multiCardGroupNo;
    }

    public void setMultiCardGroupNo(int multiCardGroupNo) {
        this.multiCardGroupNo = multiCardGroupNo;
    }

    public int getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(int accessChannel) {
        this.accessChannel = accessChannel;
    }

    public int getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(int deviceNo) {
        this.deviceNo = deviceNo;
    }

    public int getDistractControlNo() {
        return distractControlNo;
    }

    public void setDistractControlNo(int distractControlNo) {
        this.distractControlNo = distractControlNo;
    }

    public int getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(int employeeNo) {
        this.employeeNo = employeeNo;
    }

    public int getLocalControllerID() {
        return localControllerID;
    }

    public void setLocalControllerID(int localControllerID) {
        this.localControllerID = localControllerID;
    }
}
