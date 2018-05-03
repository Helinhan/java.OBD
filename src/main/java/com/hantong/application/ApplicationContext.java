package com.hantong.application;

import com.hantong.db.DatabaseManager;
import com.hantong.model.ServerConfig;
import com.hantong.model.StrategyConfig;
import com.hantong.model.StrategyType;
import com.hantong.service.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    private static ServiceManager serviceManager;

    private static DatabaseManager databaseManager;
}
