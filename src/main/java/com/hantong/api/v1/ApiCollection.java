package com.hantong.api.v1;

import com.hantong.db.IndicatorCollection;
import com.hantong.processor.CollectionProcessor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/collection")
public class ApiCollection {

    @Autowired
    private CollectionProcessor processor;

    @ApiOperation(value="获取收集信息", notes="获取收集信息")
    @RequestMapping(value="/data/{device}/{begin}/{end}",method= RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public List<IndicatorCollection> getCollectionData(@PathVariable("device") String device, @PathVariable("begin") Long begin, @PathVariable("end") Long end) {
        return processor.findCollDataByDeviceDate(device,begin,end);
    }
}
