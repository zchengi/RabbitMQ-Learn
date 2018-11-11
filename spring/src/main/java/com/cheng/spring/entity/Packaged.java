package com.cheng.spring.entity;

import java.io.Serializable;

/**
 * @author cheng
 *         2018/11/9 15:12
 */
public class Packaged implements Serializable {

    private static final long serialVersionUID = -2193996959848816664L;

    private String id;
    private String name;
    private String description;

    public Packaged() {
    }

    public Packaged(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
