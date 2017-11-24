package com.xlibao.passport.data.mapper.channel;

import com.xlibao.metadata.passport.PassportChannel;
import com.xlibao.passport.data.mapper.partner.PassportPartnerApplicationMapper;
import com.xlibao.passport.data.mapper.partner.PassportPartnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/12.
 */
@Component
public class ChannelDataManager {

    @Autowired
    private PassportPartnerMapper passportPartnerMapper;
    @Autowired
    private PassportPartnerApplicationMapper passportPartnerApplicationMapper;
    @Autowired
    private PassportChannelMapper passportChannelMapper;

    public PassportChannel getChannel(long channelId) {
        return passportChannelMapper.getChannel(channelId);
    }
}