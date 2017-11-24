package com.xlibao.market.protocol;

/**
 * @author chinahuangxc on 2017/8/10.
 */
public interface HardwareMessageType {

    /** 硬件消息起始符 */
    String HARDWARE_MSG_START = "AAAA";
    /** 硬件消息结束符 */
    String HARDWARE_MSG_END = "FFFF";

    /** 错误标志位 */
    String ERROR_MSG = "EE";

    /** 发送配货货命令（将商品放到预存箱）即：出货 */
    String SHIPMENT = "0001";

    /** 获取货架信息 */
    String SHELVES = "0002";

    /** 获取订单信息 */
    String ORDER = "0003";

    /** 退货 */
    String REFUND = "0004";

    /** 发送取货货命令（打开预存箱柜门） */
    String PICK_UP = "0005";

    /** 报警信息与心跳包 */
    String WARN = "0006";
}