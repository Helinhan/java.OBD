package com.hantong.outbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.apache.log4j.Logger;


public class DefaultOutboundProcessor extends OutboundProcessor {
    private Logger LOGGER = Logger.getLogger(DefaultOutboundProcessor.class);
    @Override
    public ErrorCode postReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {

        LOGGER.info("postReceiveMessage");
        return ErrorCode.Success;
    }
}
