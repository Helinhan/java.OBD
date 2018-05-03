package com.hantong.api.v1;

import com.hantong.HantongApplication;
import com.hantong.application.ApplicationContext;
import com.hantong.model.ServerConfig;
import com.hantong.model.StrategyConfig;
import com.hantong.model.StrategyType;
import com.hantong.module.ServiceModule;
import com.hantong.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/api/v1/")
public class Service {

    @Autowired
    private ServiceModule serviceModule;

    @ApiOperation(value="添加一个服务", notes="添加一个服务")
    @RequestMapping(value="/service",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map addService() {
        return serviceModule.addDefaultService();
    }

    @ApiOperation(value="查询所有服务", notes="查询所有服务")
    @RequestMapping(value="/services",method= RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result getAllService() {
        return serviceModule.getAllService();
    }
}
