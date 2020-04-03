package com.keydak.hc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 门禁状态
 */
public class AcsWorkDTO {
    private Boolean[] caseStatus; // 事件报警输入状态：0- 无输入，1- 有输入
    private int batteryVoltage; // 蓄电池电压值，实际值乘10，单位：伏特
    private boolean batteryLowVoltage; // 蓄电池是否处于低压状态：0- 否，1- 是
    private int powerSupplyStatus; // 设备供电状态：1- 交流电供电，2- 蓄电池供电
    private boolean multiDoorInterlockStatus; // 多门互锁状态：0- 关闭，1- 开启
    private boolean antiSneakStatus; // 反潜回状态：0-关闭，1-开启
    private boolean hostAntiDismantleStatus; // 主机防拆状态：0- 关闭，1- 开启
    private boolean indicatorLightStatus; // 指示灯状态，0-掉线，1-在线

    private int dwCardNum; // 已添加的卡数量
    private int fireAlarmStatus; // 消防报警状态显示：0-正常、1-短路报警、2-断开报警
    private int batteryChargeStatus; // 电池充电状态：0-无效；1-充电中；2-未充电
    private int masterChannelControllerStatus; // 主通道控制器在线状态：0-无效；1-不在线；2-在线
    private int slaveChannelControllerStatus; // 从通道控制器在线状态：0-无效；1-不在线；2-在线
    private int antiSneakServerStatus; // 反潜回服务器状态：0-无效，1-未启用，2-正常，3-断开
    private int dwWhiteFaceNum; // 已添加的白名单人脸数量（通过能力集判断）
    private int dwBlackFaceNum; // 已添加的黑名单人脸数量（通过能力集判断）
    private List<Door> doors = new ArrayList<>();
    private List<CardReader> cardReaders = new ArrayList<>();
    private List<AlarmHost> alarmHosts = new ArrayList<>();

    public static class Door {
        private int lockStatus; // 门锁状态（或者梯控的继电器开合状态）：0- 正常关，1- 正常开，2- 短路报警，3- 断路报警，4- 异常报警
        private int status; // 门状态（或者梯控的楼层状态）：1- 休眠，2- 常开状态（对于梯控，表示自由状态），3- 常闭状态（对于梯控，表示禁用状态），4- 普通状态（对于梯控，表示受控状态）
        private int magneticStatus; // 门磁状态，0-正常关，1-正常开，2-短路报警，3-断路报警，4-异常报警

        public int getLockStatus() {
            return lockStatus;
        }

        public void setLockStatus(int lockStatus) {
            this.lockStatus = lockStatus;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getMagneticStatus() {
            return magneticStatus;
        }

        public void setMagneticStatus(int magneticStatus) {
            this.magneticStatus = magneticStatus;
        }
    }

    public static class CardReader {
        private boolean cardReaderOnlineStatus; // 读卡器在线状态：0- 不在线，1- 在线
        private boolean cardReaderAntiDismantleStatus; // 读卡器防拆状态：0- 关闭，1- 开启
        private int cardReaderVerifyMode; // 读卡器当前验证方式：0- 无效，1- 休眠，2- 刷卡+密码，3- 刷卡，4- 刷卡或密码，5- 指纹，6- 指纹加密码，7- 指纹或刷卡，8- 指纹加刷卡，9- 指纹加刷卡加密码（无先后顺序），10- 人脸或指纹或刷卡或密码，11- 人脸+指纹，12- 人脸+密码，13- 人脸+刷卡，14- 人脸，15- 工号+密码，16- 指纹或密码，17- 工号+指纹，18- 工号+指纹+密码，19- 人脸+指纹+刷卡，20- 人脸+密码+指纹，21- 工号+人脸

        public boolean isCardReaderOnlineStatus() {
            return cardReaderOnlineStatus;
        }

        public void setCardReaderOnlineStatus(boolean cardReaderOnlineStatus) {
            this.cardReaderOnlineStatus = cardReaderOnlineStatus;
        }

        public boolean isCardReaderAntiDismantleStatus() {
            return cardReaderAntiDismantleStatus;
        }

        public void setCardReaderAntiDismantleStatus(boolean cardReaderAntiDismantleStatus) {
            this.cardReaderAntiDismantleStatus = cardReaderAntiDismantleStatus;
        }

        public int getCardReaderVerifyMode() {
            return cardReaderVerifyMode;
        }

        public void setCardReaderVerifyMode(int cardReaderVerifyMode) {
            this.cardReaderVerifyMode = cardReaderVerifyMode;
        }
    }

    public static class AlarmHost {
        private boolean setupAlarmStatus; // 报警输入口布防状态：0- 对应报警输入口处于撤防状态，1- 对应报警输入口处于布防状态
        private boolean alarmInStatus; // 按字节表示报警输入口状态：0- 对应报警输入口当前无报警，1- 对应报警输入口当前有报警
        private boolean alarmOutStatus; // 按字节表示报警输出口状态：0- 对应报警输出口无报警，1- 对应报警输出口有报警

        public boolean getSetupAlarmStatus() {
            return setupAlarmStatus;
        }

        public void setSetupAlarmStatus(boolean setupAlarmStatus) {
            this.setupAlarmStatus = setupAlarmStatus;
        }

        public boolean getAlarmInStatus() {
            return alarmInStatus;
        }

        public void setAlarmInStatus(boolean alarmInStatus) {
            this.alarmInStatus = alarmInStatus;
        }

        public boolean getAlarmOutStatus() {
            return alarmOutStatus;
        }

        public void setAlarmOutStatus(boolean alarmOutStatus) {
            this.alarmOutStatus = alarmOutStatus;
        }
    }

    public Boolean[] getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Boolean[] caseStatus) {
        this.caseStatus = caseStatus;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public boolean isBatteryLowVoltage() {
        return batteryLowVoltage;
    }

    public void setBatteryLowVoltage(boolean batteryLowVoltage) {
        this.batteryLowVoltage = batteryLowVoltage;
    }

    public int getPowerSupplyStatus() {
        return powerSupplyStatus;
    }

    public void setPowerSupplyStatus(int powerSupplyStatus) {
        this.powerSupplyStatus = powerSupplyStatus;
    }

    public boolean isMultiDoorInterlockStatus() {
        return multiDoorInterlockStatus;
    }

    public void setMultiDoorInterlockStatus(boolean multiDoorInterlockStatus) {
        this.multiDoorInterlockStatus = multiDoorInterlockStatus;
    }

    public boolean isAntiSneakStatus() {
        return antiSneakStatus;
    }

    public void setAntiSneakStatus(boolean antiSneakStatus) {
        this.antiSneakStatus = antiSneakStatus;
    }

    public boolean isHostAntiDismantleStatus() {
        return hostAntiDismantleStatus;
    }

    public void setHostAntiDismantleStatus(boolean hostAntiDismantleStatus) {
        this.hostAntiDismantleStatus = hostAntiDismantleStatus;
    }

    public boolean isIndicatorLightStatus() {
        return indicatorLightStatus;
    }

    public void setIndicatorLightStatus(boolean indicatorLightStatus) {
        this.indicatorLightStatus = indicatorLightStatus;
    }

    public int getDwCardNum() {
        return dwCardNum;
    }

    public void setDwCardNum(int dwCardNum) {
        this.dwCardNum = dwCardNum;
    }

    public int getFireAlarmStatus() {
        return fireAlarmStatus;
    }

    public void setFireAlarmStatus(int fireAlarmStatus) {
        this.fireAlarmStatus = fireAlarmStatus;
    }

    public int getBatteryChargeStatus() {
        return batteryChargeStatus;
    }

    public void setBatteryChargeStatus(int batteryChargeStatus) {
        this.batteryChargeStatus = batteryChargeStatus;
    }

    public int getMasterChannelControllerStatus() {
        return masterChannelControllerStatus;
    }

    public void setMasterChannelControllerStatus(int masterChannelControllerStatus) {
        this.masterChannelControllerStatus = masterChannelControllerStatus;
    }

    public int getSlaveChannelControllerStatus() {
        return slaveChannelControllerStatus;
    }

    public void setSlaveChannelControllerStatus(int slaveChannelControllerStatus) {
        this.slaveChannelControllerStatus = slaveChannelControllerStatus;
    }

    public int getAntiSneakServerStatus() {
        return antiSneakServerStatus;
    }

    public void setAntiSneakServerStatus(int antiSneakServerStatus) {
        this.antiSneakServerStatus = antiSneakServerStatus;
    }

    public int getDwWhiteFaceNum() {
        return dwWhiteFaceNum;
    }

    public void setDwWhiteFaceNum(int dwWhiteFaceNum) {
        this.dwWhiteFaceNum = dwWhiteFaceNum;
    }

    public int getDwBlackFaceNum() {
        return dwBlackFaceNum;
    }

    public void setDwBlackFaceNum(int dwBlackFaceNum) {
        this.dwBlackFaceNum = dwBlackFaceNum;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public void addDoors(Door door) {
        this.doors.add(door);
    }

    public List<CardReader> getCardReaders() {
        return cardReaders;
    }

    public void setCardReaders(List<CardReader> cardReaders) {
        this.cardReaders = cardReaders;
    }

    public void addCardReaders(CardReader cardReader) {
        this.cardReaders.add(cardReader);
    }

    public List<AlarmHost> getAlarmHosts() {
        return alarmHosts;
    }

    public void setAlarmHosts(List<AlarmHost> alarmHosts) {
        this.alarmHosts = alarmHosts;
    }

    public void addAlarmHosts(AlarmHost alarmHost) {
        this.alarmHosts.add(alarmHost);
    }
}
