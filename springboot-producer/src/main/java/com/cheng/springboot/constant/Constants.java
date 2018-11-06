package com.cheng.springboot.constant;

/**
 * @author cheng
 *         2018/11/7 0:13
 */
public final class Constants {

    /**
     * 消息状态
     */
    public static final String ORDER_SENDING = "0";
    public static final String ORDER_SEND_SUCCESS = "1";
    public static final String ORDER_SEND_FAILURE = "2";

    /**
     * 消息超时时间（默认单位: min）
     */
    public static final int ORDER_TIMEOUT = 1;
}
