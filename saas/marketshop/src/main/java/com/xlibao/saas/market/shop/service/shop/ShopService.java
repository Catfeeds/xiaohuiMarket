package com.xlibao.saas.market.shop.service.shop;

import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.io.service.netty.NettySession;

/**
 * @author chinahuangxc on 2017/8/9.
 */
public interface ShopService {

    MessageOutputStream securityVerification(MessageInputStream message, NettySession session);

    void hardwareMessage(MessageInputStream message, NettySession session);
}