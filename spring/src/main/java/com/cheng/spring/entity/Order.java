package com.cheng.spring.entity;

import java.io.Serializable;

/**
 * @author cheng
 *         2018/11/9 15:11
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 6486139780015998377L;

    private String id;
    private String name;
    private String content;

    public Order() {
    }

    public Order(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
