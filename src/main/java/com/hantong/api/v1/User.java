package com.hantong.api.v1;

import com.hantong.code.ErrorCode;
import com.hantong.code.Role;
import com.hantong.model.ServerConfig;
import com.hantong.result.Result;
import com.hantong.user.UserManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.hantong.service.ServiceModule;

import static com.hantong.code.ErrorCode.ServiceNotExist;

@RestController
@RequestMapping(value="/api/v1/user")
//@Secured({Role.ADMIN})
public class User {
    @Autowired
    private UserManager userManager;
    @ApiOperation(value="添加一个用户", notes="添加一个用户")
    @RequestMapping(value="/add",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public ErrorCode addService(@RequestParam("name") String name,@RequestParam("password") String password,@RequestParam("role") String role) {

        return userManager.addUser(name,password,role);
    }

    @ApiOperation(value="添加一个用户", notes="添加一个用户")
    @RequestMapping(value="/del",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public ErrorCode delService(String name) {

        return userManager.delUser(name);
    }

}
