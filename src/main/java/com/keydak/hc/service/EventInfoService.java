package com.keydak.hc.service;

import com.keydak.hc.entity.EventInfo;
import com.keydak.hc.repository.EventInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class EventInfoService implements IEventInfoService {
    @Resource
    private EventInfoRepository eventInfoRepository;

    @Override
    public void save(EventInfo eventInfo) {
        eventInfoRepository.save(eventInfo);
    }

    @Override
    public Page<EventInfo> findAllByPage(Pageable pageable) {
        return eventInfoRepository.findAll(pageable);
    }
}
