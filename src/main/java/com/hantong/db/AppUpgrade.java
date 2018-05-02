package com.hantong.db;

import org.springframework.data.annotation.Id;

public class AppUpgrade {
    @Id
    public String id;
    public String ver;
    public boolean canUpgrade = true;
    public boolean forceUpgrade = false;
    public String toVersion = "latest";
    public String appType = "person";
    public String appVerUrl;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public boolean isCanUpgrade() {
        return canUpgrade;
    }

    public void setCanUpgrade(boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getToVersion() {
        return toVersion;
    }

    public void setToVersion(String toVersion) {
        this.toVersion = toVersion;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVerUrl() {
        return appVerUrl;
    }

    public void setAppVerUrl(String appVerUrl) {
        this.appVerUrl = appVerUrl;
    }


}
