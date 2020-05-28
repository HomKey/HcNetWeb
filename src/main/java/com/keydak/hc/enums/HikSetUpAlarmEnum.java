package com.keydak.hc.enums;

public class HikSetUpAlarmEnum {
    //智能交通布防优先级：0- 一等级（高），1- 二等级（中），2- 三等级（低）
    public enum Level {
        HIGH((byte) 0),
        MIDDLE((byte) 1),
        LOW((byte) 2),
        ;
        private byte value;

        Level(byte value) {
            this.value = value;
        }
        public byte getValue() {
            return value;
        }
    }
    //智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
    public enum AlarmInfoType{
        OLD((byte)0),
        NEW((byte)1),
        ;
        private byte value;

        AlarmInfoType(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }
    //布防类型(仅针对门禁主机、人证设备)：0-客户端布防(会断网续传)，1-实时布防(只上传实时数据)
    public enum DeployType{
        CLIENT((byte) 0 ),
        REALTIME((byte) 1)
        ;
        private byte value;

        DeployType(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

}
