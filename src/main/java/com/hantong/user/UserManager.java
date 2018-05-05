package com.hantong.user;

import com.hantong.application.ApplicationContext;
import com.hantong.code.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserManager {

    @Autowired
    PasswordEncoder passwordEncoder;

    public User findUser(String name) {
        return ApplicationContext.getDatabaseManager().getDbInstance().findUser(name);
    }

    public ErrorCode addUser(String userName, String password, String role) {
        password = passwordEncoder.encode(password);
        User user = new User();
        user.setName(userName);
        user.setPassword(password);
        List<String> roles = new ArrayList<>();
        roles.add(role);
        user.setRole(roles);
        return ApplicationContext.getDatabaseManager().getDbInstance().addUser(user);
    }

    public List<User> findUserByRole(String role) {
        return ApplicationContext.getDatabaseManager().getDbInstance().findUserByRole(role);
    }
}
