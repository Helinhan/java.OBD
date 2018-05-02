package com.hantong.util;

import org.springframework.boot.ApplicationHome;

import java.io.File;

public class YjUtil {
    public static String getRootPath(){
        ApplicationHome home = new ApplicationHome(YjUtil.class);
        File jarFile = home.getSource();
        return jarFile.getParent();
    }
}
