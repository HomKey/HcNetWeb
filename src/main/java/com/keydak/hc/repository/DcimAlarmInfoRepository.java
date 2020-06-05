package com.keydak.hc.repository;

import com.keydak.hc.entity.DcimAlarmInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DcimAlarmInfoRepository extends JpaRepository<DcimAlarmInfo, String> {
}
