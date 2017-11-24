package com.xlibao.passport.data.mapper.partner;

import com.xlibao.metadata.passport.PassportPartner;
import org.apache.ibatis.annotations.Param;

public interface PassportPartnerMapper {

    PassportPartner getPartner(@Param("id") long id);
}