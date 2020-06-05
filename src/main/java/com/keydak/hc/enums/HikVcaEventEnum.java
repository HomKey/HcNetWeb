package com.keydak.hc.enums;

public enum HikVcaEventEnum {
    TRAVERSE_PLANE(1, "穿越警戒面（越界侦测）"),
    ENTER_AREA(2, "目标进入区域，支持区域规则"),
    EXIT_AREA(3, "目标离开区域，支持区域规则"),
    INTRUSION(4, "周界入侵（区域入侵侦测），支持区域规则"),
    LOITER(5, "徘徊，支持区域规则"),
    LEFT_TAKE(6, "丢包捡包，支持区域规则"),
    PARKING(7, "停车，支持区域规则"),
    RUN(8, "快速移动(奔跑)，支持区域规则"),
    HIGH_DENSITY(9, "区域内人员密度，支持区域规则，人员聚集度超过设置的阈值时设备上传报警信息"),
    VIOLENT_MOTION(10, "剧烈运动检测"),
    REACH_HIGHT(11, "攀高检测"),
    GET_UP(12, "起身检测"),
    LEFT(13, "物品遗留"),
    TAKE(14, "物品拿取"),
    LEAVE_POSITION(15, "离岗"),
    TRAIL(16, "尾随"),
    KEY_PERSON_GET_UP(17, "重点人员起身检测"),
    STANDUP(18, "起立检测"),
    FALL_DOWN(20, "倒地检测"),
    AUDIO_ABNORMAL(21, "声强突变检测"),
    ADV_REACH_HEIGHT(22, "折线攀高"),
    TOILET_TARRY(23, "如厕超时"),
    YARD_TARRY(24, "放风场滞留"),
    ADV_TRAVERSE_PLANE(25, "折线警戒面"),
    LECTURE(26, "授课(文教)"),
    ANSWER(27, "回答问题(文教)"),
    HUMAN_ENTER(29, "人靠近ATM（仅在ATM_PANEL模式下支持）"),
    OVER_TIME(30, "操作超时（仅在ATM_PANEL模式下支持）"),
    STICK_UP(31, "贴纸条,支持区域规则"),
    INSTALL_SCANNER(32, "安装读卡器,支持区域规则"),
    PEOPLCHANGE(35, "人数变化事件"),
    SPACING_CHANGE(36, "间距变化事件"),
    COMBINED_RULE(37, "组合规则事件"),
    SIT_QUIETLY(38, "一动不动（静坐）事件"),
    HIGH_DENSITY_STATUS(39, "区域内人员聚集状态，设备按照设置的时间间隔上传实时的人员聚集状态信息，该时间间隔不支持通过SDK配置，需要通过服务器的配置文件来修改，默认：10s"),
    RUNNING(40, "奔跑检测"),
    RETENTION(41, "滞留检测"),
    BLACKBOARD_WRITE(42, "板书"),
    ;

    private int value;
    private String message;

    HikVcaEventEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }


    public static String getMessage(int value){
        for (HikVcaEventEnum e : HikVcaEventEnum.values()) {
            if (e.value == value) return e.message;
        }
        return "";
    }
    public static HikVcaEventEnum get(int value){
        for (HikVcaEventEnum e : HikVcaEventEnum.values()) {
            if (e.value == value) return e;
        }
        return null;
    }
}
