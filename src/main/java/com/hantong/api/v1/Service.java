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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/api/v1/")
public class Service {

    @Autowired
    private ServiceModule serviceModule;

    @ApiOperation(value="添加一个测试服务", notes="添加一个测试服务")
    @RequestMapping(value="/testService",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map addTestService() {
        return serviceModule.addDefaultService();
    }

    @ApiOperation(value="添加一个服务", notes="添加一个服务")
    @RequestMapping(value="/service",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result addService(@RequestBody ServerConfig serverParam) {
        return serviceModule.addService(serverParam);
    }

    @ApiOperation(value="启动一个服务", notes="启动一个服务")
    @RequestMapping(value="/service/{serviceId}",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result startService(@PathVariable(value = "serviceId") String serverId) {
        return serviceModule.startService(serverId);
    }

    @ApiOperation(value="停止一个服务", notes="停止一个服务")
    @RequestMapping(value="/service/{serviceId}",method= RequestMethod.PUT, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result stopService(@PathVariable(value = "serviceId") String serverId) {
        return serviceModule.stopService(serverId);
    }

    @ApiOperation(value="删除一个服务", notes="删除一个服务")
    @RequestMapping(value="/service/{serviceId}",method= RequestMethod.DELETE, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result delService(@PathVariable(value = "serviceId") String serverId) {
        return serviceModule.delService(serverId);
    }

    @ApiOperation(value="查询所有服务", notes="查询所有服务")
    @RequestMapping(value="/services",method= RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result getAllService() {
        return serviceModule.getAllService();
    }
}
