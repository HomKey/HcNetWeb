package com.keydak.hc.core.structure;

import com.sun.jna.Structure;

public class AcsWorkStatus {
    public static final int NET_DVR_ACS_WORK_STATUS_V50 = 2180;
    private static final int MAX_DOOR_NUM_256 = 256;
    private static final int MAX_CASE_SENSOR_NUM_8 = 8;
    private static final int MAX_CARD_READER_NUM_512 = 512;
    private static final int MAX_ALARMHOST_ALARMIN_NUM_512 = 512;
    private static final int MAX_ALARMHOST_ALARMOUT_NUM_512 = 512;

    public static class NET_DVR_ACS_WORK_STATUS_V50 extends Structure {/* 门禁主机工作状态结构体 */
        public int dwSize; /* 结构大小 */
        public byte[] byDoorLockStatus = new byte[MAX_DOOR_NUM_256];
        public byte[] byDoorStatus = new byte[MAX_DOOR_NUM_256];
        public byte[] byMagneticStatus = new byte[MAX_DOOR_NUM_256];
        public byte[] byCaseStatus = new byte[MAX_CASE_SENSOR_NUM_8];
        public short wBatteryVoltage;
        public byte byBatteryLowVoltage;
        public byte byPowerSupplyStatus;
        public byte byMultiDoorInterlockStatus;
        public byte byAntiSneakStatus;
        public byte byHostAntiDismantleStatus;
        public byte byIndicatorLightStatus;
        public byte[] byCardReaderOnlineStatus = new byte[MAX_CARD_READER_NUM_512];
        public byte[] byCardReaderAntiDismantleStatus = new byte[MAX_CARD_READER_NUM_512];
        public byte[] byCardReaderVerifyMode = new byte[MAX_CARD_READER_NUM_512];
        public byte[] bySetupAlarmStatus = new byte[MAX_ALARMHOST_ALARMIN_NUM_512];
        public byte[] byAlarmInStatus = new byte[MAX_ALARMHOST_ALARMIN_NUM_512];
        public byte[] byAlarmOutStatus = new byte[MAX_ALARMHOST_ALARMOUT_NUM_512];
        public int dwCardNum;
        public byte byFireAlarmStatus;
        public byte byBatteryChargeStatus;
        public byte byMasterChannelControllerStatus;
        public byte bySlaveChannelControllerStatus;
        public byte byAntiSneakServerStatus;
        public byte[] byRes3 = new byte[3];
        public int dwWhiteFaceNum;
        public int dwBlackFaceNum;
        public byte[] byRes2 = new byte[108];
    }
}
