package com.hantong.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCommandLineRunner  implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("<<<<<<<<<<<<启动完成>>>>>>>>>>>>>>");
    }
}
