package com.cheng.springboot.config.database;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author cheng
 *         2018/11/6 23:30
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
