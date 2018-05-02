package com.hantong.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicatorInfoRepository extends JpaRepository<IndicatorInfo,Long> {
    List<IndicatorInfo> findByDevice(String device);
}
