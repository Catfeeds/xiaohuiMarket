package com.xlibao.saas.market.core.service.hardware;

import com.xlibao.io.service.netty.NettySession;

/**
 * @author chinahuangxc on 2017/8/13.
 */
public interface HardwareService {

    void fromHardwareMessageExecute(NettySession session, String content);
}