package com.hantong.interfaces;

import com.hantong.code.ErrorCode;

public interface ILifecycle {
    ErrorCode lifeStart();
    ErrorCode lifeStop();
}
