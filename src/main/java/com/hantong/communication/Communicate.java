package com.hantong.communication;

import com.hantong.interfaces.ICodec;
import com.hantong.interfaces.ICommunication;
import com.hantong.interfaces.ILifecycle;

public abstract class Communicate implements ICommunication,ILifecycle {
    public Communicate(ICodec code) {
        this.encoderDecoder = code;
    }


    protected ICodec encoderDecoder;
}
