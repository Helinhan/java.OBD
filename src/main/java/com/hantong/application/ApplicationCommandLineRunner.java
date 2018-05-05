package com.hantong.application;

import com.hantong.util.Echo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCommandLineRunner  implements CommandLineRunner {
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        applicationContext.startUp();
    }
}
