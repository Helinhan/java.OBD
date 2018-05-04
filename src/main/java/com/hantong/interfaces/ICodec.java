package com.hantong.interfaces;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

public interface ICodec {
    RequestMessage decode(byte[] data);
    byte[] encode(RequestMessage requestMessage, RuntimeMessage runtimeMessage);
}
