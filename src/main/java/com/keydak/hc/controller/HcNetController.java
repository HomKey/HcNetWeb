package com.keydak.hc.controller;

import com.keydak.hc.dto.ResponseResult;
import com.keydak.hc.entity.EventInfo;
import com.keydak.hc.enums.ResponseEnum;
import com.keydak.hc.service.IEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weigeng")
public class HcNetController {

    @Autowired
    private IEventInfoService eventInfoService;

    @GetMapping("/list")
    public ResponseResult findAllByPage(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "alarmTime");
        Page<EventInfo> eventInfoList = eventInfoService.findAllByPage(pageRequest);
        return new ResponseResult(ResponseEnum.SUCCESS, eventInfoList);
    }
}
