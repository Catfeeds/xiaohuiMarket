package com.xlibao.passport.data.mapper.partner;

import com.xlibao.metadata.passport.PassportPartner;
import com.xlibao.metadata.passport.PassportPartnerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/12.
 */
@Component
public class PartnerDataManager {

    @Autowired
    private PassportPartnerMapper passportPartnerMapper;
    @Autowired
    private PassportPartnerApplicationMapper passportPartnerApplicationMapper;
    @Autowired
    private PassportPartnerApplicationPropertiesMapper passportPartnerApplicationPropertiesMapper;

    public PassportPartner getPartner(long id) {
        return passportPartnerMapper.getPartner(id);
    }

    public PassportPartnerApplication getPartnerApp(long id) {
        return passportPartnerApplicationMapper.getPartnerApp(id);
    }
}