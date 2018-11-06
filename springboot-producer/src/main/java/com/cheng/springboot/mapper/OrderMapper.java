package com.cheng.springboot.mapper;


import com.cheng.springboot.entity.Order;

public interface OrderMapper {

    int deleteByPrimaryKey(String id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}