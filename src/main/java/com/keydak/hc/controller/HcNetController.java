package com.keydak.hc.controller;

import com.keydak.hc.dto.AcsWorkDTO;
import com.keydak.hc.dto.ResponseResult;
import com.keydak.hc.entity.EventInfo;
import com.keydak.hc.enums.ResponseEnum;
import com.keydak.hc.service.IEventInfoService;
import com.keydak.hc.service.IHcDoorAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hc")
public class HcNetController {

    @Autowired
    private IEventInfoService eventInfoService;
    @Autowired
    private IHcDoorAccessService hcDoorAccessService;

    /**
     * 门禁历史记录（读数据库）
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @GetMapping("/list")
    public ResponseResult findAllByPage(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.DESC, "alarmTime");
        Page<EventInfo> eventInfoList = eventInfoService.findAllByPage(pageRequest);
        return new ResponseResult(ResponseEnum.SUCCESS, eventInfoList);
    }

    /**
     * 门禁状态（读内存数据）
     * @param doorCount
     * @param cardCount
     * @param alarmCount
     * @return
     */
    @CrossOrigin
    @GetMapping("/acsWork")
    public ResponseResult getAcsWork(
            @RequestParam(value = "doorCount", defaultValue = "0", required = false) Integer doorCount,
            @RequestParam(value = "cardCount", defaultValue = "0", required = false) Integer cardCount,
            @RequestParam(value = "alarmCount", defaultValue = "0", required = false) Integer alarmCount) {
        return new ResponseResult(ResponseEnum.SUCCESS, hcDoorAccessService.getAcsWordData(doorCount, cardCount, alarmCount));
    }
}
