package com.hantong.outbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.apache.log4j.Logger;

public class SourceReplyOutboundProcessor  extends OutboundProcessor  {
    private Logger LOGGER = Logger.getLogger(SourceReplyOutboundProcessor.class);
    @Override
    public ErrorCode postReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        LOGGER.info("postReceiveMessage");
        if (runtimeMessage.getCommunicationSource() == null || runtimeMessage.getContext() == null) {
            return ErrorCode.ServiceNotExist;
        }

        Long begin = System.currentTimeMillis();
        ErrorCode result =  runtimeMessage.getCommunicationSource().sendData(requestMessage,runtimeMessage);
        Long end = System.currentTimeMillis();
        runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,end);
        return result;
    }
}
