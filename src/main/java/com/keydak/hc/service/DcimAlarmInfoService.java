package com.keydak.hc.service;

import com.keydak.hc.entity.DcimAlarmInfo;
import com.keydak.hc.repository.DcimAlarmInfoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class DcimAlarmInfoService implements IDcimAlarmInfoService{

    @Resource
    private DcimAlarmInfoRepository dcimAlarmInfoRepository;

    @Override
    public void save(DcimAlarmInfo dcimAlarmInfo) {
        dcimAlarmInfoRepository.save(dcimAlarmInfo);
    }
}
