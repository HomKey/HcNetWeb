package com.keydak.hc.enums;

public enum HikVcaEventEnum {
    VCA_TRAVERSE_PLANE(0x1,"穿越警戒面"),
    VCA_ENTER_AREA(0x2,"目标进入区域"),
    VCA_EXIT_AREA(0x4,"目标离开区域"),
    VCA_INTRUSION(0x8,"周界入侵"),
    VCA_LOITER(0x10,"徘徊"),
    VCA_LEFT_TAKE(0x20,"丢包捡包"),
    VCA_PARKING(0x40,"停车"),
    VCA_RUN(0x80,"奔跑"),
    VCA_HIGH_DENSITY(0x100,"区域内人员密度"),
    VCA_VIOLENT_MOTION(0x200,"剧烈运动检测"),
    VCA_REACH_HIGHT(0x400,"攀高检测"),
    VCA_GET_UP(0x800,"起身检测"),
    VCA_LEFT(0x1000,"丢包"),
    VCA_TAKE(0x2000,"捡包"),
    VCA_LEAVE_POSITION(0x4000,"离岗"),
    VCA_TRAIL(0x8000,"尾随"),
    VCA_KEY_PERSON_GET_UP(0x10000,"重点人员起身检测"),
    VCA_STANDUP(0x20000,"起立检测"),
    VCA_FALL_DOWN(0x80000,"倒地检测"),
    VCA_AUDIO_ABNORMAL(0x100000,"声强突变检测"),
    VCA_ADV_REACH_HEIGHT(0x200000,"折线攀高"),
    VCA_TOILET_TARRY(0x400000,"如厕超时"),
    VCA_YARD_TARRY(0x800000,"放风场滞留"),
    VCA_ADV_TRAVERSE_PLANE(0x1000000,"折线警戒面"),
    VCA_HUMAN_ENTER(0x10000000,"人靠近ATM（仅在ATM_PANEL模式下支持）"),
    VCA_OVER_TIME(0x20000000,"操作超时（仅在ATM_PANEL模式下支持）"),
    VCA_STICK_UP(0x40000000,"贴纸条,支持区域规则"),
    VCA_INSTALL_SCANNER(0x80000000,"安装读卡器,支持区域规则")
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
    //    enum _VCA_EVENT_TYPE_{  }VCA_EVENT_TYPE
}
