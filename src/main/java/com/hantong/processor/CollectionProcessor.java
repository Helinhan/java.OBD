package com.hantong.processor;

import com.hantong.db.IndicatorCollection;
import com.hantong.db.IndicatorCollectionRepository;
import com.hantong.db.IndicatorInfo;
import com.hantong.db.IndicatorInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CollectionProcessor {
    @Autowired
    private IndicatorInfoRepository indicatorInfoRepository;

    @Autowired
    private IndicatorCollectionRepository indicatorCollectionRepository;

    public List<IndicatorCollection> findCollDataByDeviceDate(String device,Long begin,Long end) {
        List<IndicatorInfo> indicatorInfo = indicatorInfoRepository.findByDevice(device);

        List<IndicatorCollection> result = new ArrayList<>();

        for (IndicatorInfo info : indicatorInfo) {
            List<IndicatorCollection> coll = indicatorCollectionRepository.findByIndicatorInfoAndTimestampBetween(info,begin,end);

            if (null == coll) continue;

            result.addAll(coll);
        }

        return result;
    }
}
