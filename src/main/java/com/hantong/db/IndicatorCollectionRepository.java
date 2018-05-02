package com.hantong.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicatorCollectionRepository  extends JpaRepository<IndicatorCollection,Long> {
    public List<IndicatorCollection> findByIndicatorInfoAndTimestampBetween(IndicatorInfo info, Long begin, Long end );
}
