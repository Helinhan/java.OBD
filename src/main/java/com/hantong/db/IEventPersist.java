package com.hantong.db;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

public interface IEventPersist {
    ErrorCode addRequestMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage);
}
