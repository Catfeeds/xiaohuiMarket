package com.xlibao.passport.service.channel.impl;

import com.xlibao.common.BasicWebService;
import com.xlibao.passport.service.channel.ChannelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/2/16.
 */
@Transactional
@Service("channelService")
public class ChannelServiceImpl extends BasicWebService implements ChannelService {
}