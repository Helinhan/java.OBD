package com.hantong.api.v1;

import com.hantong.application.ApplicationContext;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.result.Result;
import com.hantong.service.Service;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.hantong.code.ErrorCode.ServiceNotExist;

@RestController
@RequestMapping(value="/api/v1/")
public class Device {
    @ApiOperation(value="上报数据", notes="上报数据")
    @RequestMapping(value="/report",method= RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map addService(@RequestBody RequestMessage requestMessage) {

        RuntimeMessage runtimeMessage = new RuntimeMessage();
        Service service = ApplicationContext.getServiceManager().getService("Service_001");
        if (null == service) {
            Result result = new Result(ServiceNotExist);
            result.put("提示","默认创始的serverid为Service_001");
            return result;
        }
        service.getInboundStrategy().onReceiveMessage(requestMessage,runtimeMessage);
        return new Result();
    }
}
