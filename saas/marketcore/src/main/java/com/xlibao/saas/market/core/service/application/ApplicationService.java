package com.xlibao.saas.market.core.service.application;

import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.service.netty.NettySession;

/**
 * @author chinahuangxc on 2017/8/13.
 */
public interface ApplicationService {

    void scanPickUp(String orderSequenceNumber);

    void logicMessageExecute(NettySession session, MessageInputStream message);

    void platformMessageExecute(NettySession session, MessageInputStream message);

    void toHardwareMessageExecute(NettySession session, MessageInputStream message);
}