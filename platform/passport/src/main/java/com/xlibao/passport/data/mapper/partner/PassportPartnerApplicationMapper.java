package com.xlibao.passport.data.mapper.partner;

import com.xlibao.metadata.passport.PassportPartnerApplication;
import org.apache.ibatis.annotations.Param;

public interface PassportPartnerApplicationMapper {

    PassportPartnerApplication getPartnerApp(@Param("id") long id);
}