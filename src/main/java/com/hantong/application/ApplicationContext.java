package com.hantong.application;

import com.hantong.code.Role;
import com.hantong.db.DatabaseManager;
import com.hantong.user.User;
import com.hantong.service.ServiceManager;
import com.hantong.user.UserManager;
import com.hantong.util.Echo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationContext {

    public ApplicationContext() {
    }

    public static ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Autowired
    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Autowired
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private static UserManager userManager;
    private static ServiceManager serviceManager;
    private static DatabaseManager databaseManager;

    public void startUp() {
        Echo.green("<<<<<<<<<<<<启动完成>>>>>>>>>>>>>>");
        Echo.green("**用户配置检查**");
        List<User> users = userManager.findUserByRole(Role.ADMIN);
        if (null == users || users.size() ==0) {
            userManager.addUser("admin","password",Role.ADMIN);
            Echo.red(String.format("\t不存在管理员，创建默认管理帐号：用户名：%s 密码：%s","admin","password"));
        } else if (users != null) {
            for (User user : users) {
                if (user.getName().equals("admin")) {
                    Echo.yellow(String.format("\t默认管理员账号 admin 存在，安全起见建议添加其他管理员后删除该账号"));
                    break;
                }
            }
        } else {
            Echo.blue(String.format("\tOK"));
        }

    }
}
