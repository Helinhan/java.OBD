package com.hantong.processor;

import com.hantong.db.User;
import com.hantong.db.UserRepository;
import com.hantong.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserProcessor {

    @Autowired
    PasswordEncoder md5PasswordEncoder;

    @Autowired
    private UserRepository repository;

    public Result addUser(String name, String password, String[] role){

        password = md5PasswordEncoder.encode(password);
        User user = new User(name,password);

        if (null != role){
            for (String r : role) {
                user.addRole(r);
            }
        }

        repository.save(user);

        return new Result();
    }

    public User findUser(String name) {
        return repository.findByName(name);
    }
}
