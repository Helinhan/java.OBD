package com.hantong.application;

import com.hantong.code.Role;
import com.hantong.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceProvider  implements UserDetailsService {

    private void addAllRole( List<GrantedAuthority> authorities){
        try {
            Class cls = Role.class;
            Field[] fields = cls.getFields();
            for (Field field : fields) {
                authorities.add(new SimpleGrantedAuthority(field.get(cls).toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userGet = ApplicationContext.getUserManager().findUser(username);
        if (null == userGet) {
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String r : userGet.getRole()) {
            if (r.equals(Role.ADMIN)){
                this.addAllRole(authorities);
                break;
            }
            authorities.add(new SimpleGrantedAuthority(r));
        }

        SecurityUser user = new SecurityUser(username,userGet.getPassword(),authorities);

        return user;
    }
}
