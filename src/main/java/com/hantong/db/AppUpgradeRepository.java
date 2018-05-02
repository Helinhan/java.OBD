package com.hantong.db;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUpgradeRepository  extends MongoRepository<AppUpgrade, String> {
    public AppUpgrade findAppUpgradeByAppTypeAndVer(String type,String ver);
    public AppUpgrade findAppUpgradeByAppTypeOrderByVerDesc(String type);
}
