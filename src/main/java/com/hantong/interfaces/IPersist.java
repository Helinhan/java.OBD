package com.hantong.interfaces;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.user.User;

import java.util.List;

public interface IPersist {
    ErrorCode addRequestMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage);

    User findUser(String name);
    ErrorCode addUser(User user);
    List<User> findUserByRole(String role);
//    ErrorCode delUser(String name);
}
