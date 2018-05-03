package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.chain.InboundProcessorChain;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;

public class BlockInboundStrategy extends InboundStrategy {

    public BlockInboundStrategy(Service s,StrategyConfig config) {
        super(s,config);
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        ErrorCode err =  inboundProcessorChain.onReceiveMessage(requestMessage,runtimeMessage);
        if (err != ErrorCode.Success) {
            return err;
        }

        processOver(requestMessage,runtimeMessage);

        return ErrorCode.Success;
    }
}
