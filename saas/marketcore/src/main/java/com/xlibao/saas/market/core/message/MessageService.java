package com.xlibao.saas.market.core.message;

/**
 * @author chinahuangxc on 2017/8/19.
 */
public interface MessageService {

    void reconnector(MarketConnectorListener connectorListener);

    void startListener();
}