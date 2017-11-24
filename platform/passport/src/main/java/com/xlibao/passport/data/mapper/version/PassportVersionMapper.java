package com.xlibao.passport.data.mapper.version;

import com.xlibao.passport.data.model.PassportVersion;
import org.apache.ibatis.annotations.Param;

public interface PassportVersionMapper {

    PassportVersion getNewestVersion(@Param("deviceType") int deviceType, @Param("clientType") int clientType);
}