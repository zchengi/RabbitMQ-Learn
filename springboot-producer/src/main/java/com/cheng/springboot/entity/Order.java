package com.cheng.springboot.entity;

import java.io.Serializable;

/**
 * @author cheng
 *         2018/11/6 17:25
 */
public class Order implements Serializable {

    private static final long serialVersionUID = -1490177492203752405L;

    private String id;

    private String name;

    /**
     * 存储消息发送的唯一标识
     */
    private String messageId;

    public Order() {
    }

    public Order(String id, String name, String messageId) {
        this.id = id;
        this.name = name;
        this.messageId = messageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
