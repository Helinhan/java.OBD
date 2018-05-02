package com.hantong.interfaces;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

public interface IInbound {
    ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage);
}
