package com.hantong.api.v1;

import com.hantong.code.ErrorCode;
import com.hantong.code.Role;
import com.hantong.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/v1/user")
@Secured({Role.ADMIN})
public class User {


}
