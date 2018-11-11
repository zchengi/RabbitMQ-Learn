package com.cheng.spring.convert;

import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;

/**
 * 反序列化信任问题解决
 *
 * @author cheng
 *         2018/11/11 21:36
 */
public class RabbitMqJsonMessageClassMapper extends DefaultJackson2JavaTypeMapper {

    public RabbitMqJsonMessageClassMapper() {
        super();
        setTrustedPackages("*");
    }
}
