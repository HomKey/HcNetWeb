CREATE TABLE `dcim_alarm_data`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT,
    `createTime`            datetime   NOT NULL,
    `updateTime`            datetime   NOT NULL,
    `alarmContent`          varchar(255) DEFAULT NULL,
    `alarmLevel`            int(11)    NOT NULL,
    `alarmLevelDescription` varchar(255) DEFAULT NULL,
    `alarmShortContent`     varchar(255) DEFAULT NULL,
    `alarmTime`             datetime     DEFAULT NULL,
    `alarmType`             int(11)    NOT NULL,
    `alarmTypeDescription`  varchar(255) DEFAULT NULL,
    `detailStore`           varchar(255) DEFAULT NULL,
    `deviceId`              varchar(255) DEFAULT NULL,
    `deviceName`            varchar(255) DEFAULT NULL,
    `eventId`               int(11)    NOT NULL,
    `name`                  varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `hc_net_event_info`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `createTime`        datetime   NOT NULL,
    `updateTime`        datetime   NOT NULL,
    `accessChannel`     int(11)    NOT NULL,
    `alarmInNo`         int(11)    NOT NULL,
    `alarmOutNo`        int(11)    NOT NULL,
    `alarmTime`         datetime     DEFAULT NULL,
    `cardNo`            varchar(255) DEFAULT NULL,
    `cardReaderKind`    int(11)    NOT NULL,
    `cardReaderNo`      int(11)    NOT NULL,
    `cardType`          int(11)    NOT NULL,
    `caseSensorNo`      int(11)    NOT NULL,
    `channelNo`         int(11)    NOT NULL,
    `deviceNo`          int(11)    NOT NULL,
    `distractControlNo` int(11)    NOT NULL,
    `doorNo`            int(11)    NOT NULL,
    `employeeNo`        int(11)    NOT NULL,
    `localControllerID` int(11)    NOT NULL,
    `majorType`         int(11)    NOT NULL,
    `minorType`         int(11)    NOT NULL,
    `multiCardGroupNo`  int(11)    NOT NULL,
    `netUser`           varchar(255) DEFAULT NULL,
    `remoteHostAdd`     varchar(255) DEFAULT NULL,
    `reportChannel`     int(11)    NOT NULL,
    `rs485No`           int(11)    NOT NULL,
    `verifyNo`          int(11)    NOT NULL,
    `whiteListNo`       int(11)    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

