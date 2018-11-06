package com.cheng.springboot.producer;

import com.cheng.springboot.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cheng
 *         2018/11/6 17:27
 */
@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Order order) {

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());

        // exchange routingKey 消息体内容
        rabbitTemplate.convertAndSend("order-exchange",
                "order.test",
                order,
                correlationData);
    }
}
