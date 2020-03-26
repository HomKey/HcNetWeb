package com.keydak.hc.service;

import com.keydak.hc.entity.EventInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEventInfoService {
    void save(EventInfo eventInfo);
    Page<EventInfo> findAllByPage(Pageable pageable);
}