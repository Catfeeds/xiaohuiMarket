package com.xlibao.passport.data.mapper.version;

import com.xlibao.passport.data.model.PassportVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/4/17.
 */
@Component
public class VersionDataManager {

    @Autowired
    private PassportVersionMapper versionMapper;

    public PassportVersion getNewestVersion(int deviceType, int clientType) {
        return versionMapper.getNewestVersion(deviceType, clientType);
    }
}