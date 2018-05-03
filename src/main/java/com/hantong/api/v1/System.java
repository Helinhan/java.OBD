package com.hantong.api.v1;

import com.hantong.HantongApplication;
import com.hantong.code.Role;
import com.hantong.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(value="/api/v1/system")
//@Secured({Role.ADMIN})
public class System {
    @ApiOperation(value="获取系统信息", notes="获取系统信息")
    @RequestMapping(value="/info",method= RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map getSystemInfo() {
        Result result = new Result();

        result.put("startup", HantongApplication.getApplicationContext().getStartupDate());
        result.put("applicationName", HantongApplication.getApplicationContext().getApplicationName());
        result.put("displayName", HantongApplication.getApplicationContext().getDisplayName());

        return result;
    }

    @ApiOperation(value="获取系统信息", notes="获取系统加载的bean")
    @RequestMapping(value="/beans",method= RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map getSystemBeans() {

        String[] beans = HantongApplication.getApplicationContext().getBeanDefinitionNames();

        return Result.from("beans",beans);
    }
}
