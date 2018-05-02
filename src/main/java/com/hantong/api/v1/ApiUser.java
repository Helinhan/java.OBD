package com.hantong.api.v1;

import com.hantong.code.ErrorCode;
import com.hantong.code.Role;
import com.hantong.processor.UserProcessor;
import com.hantong.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/v1/user")
@Secured({Role.ADMIN})
public class ApiUser {

    @Autowired
    UserProcessor userProcessor;

    @ApiOperation(value="添加用户", notes="note")
    @RequestMapping(value="/",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Result addUser(
            @ApiParam(value = "用户名字", required = true) @RequestParam(value = "name", required = true) String userName,
            @ApiParam(value = "用户密码", required = true) @RequestParam(value = "password", required = true) String userPwd,
            @ApiParam(value = "用户权限", required = true) @RequestParam(value = "role", required = true) String userRole
    ) {

        if (this.userProcessor.findUser(userName) != null){
            return new Result(ErrorCode.UserAlreadyExist);
        }

        return userProcessor.addUser(userName,userPwd,userRole.split(","));
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询用户信息")
    public com.hantong.db.User findName(@PathVariable("name") String name){
        return this.userProcessor.findUser(name);
    }

}
