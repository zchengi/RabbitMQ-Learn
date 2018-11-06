package com.cheng.springboot.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import static org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

/**
 * @author cheng
 *         2018/11/7 0:26
 */
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        System.out.println("correlationData: " + correlationData);
        System.out.println("ack: " + ack);
        if (!ack) {
            System.out.println("异常处理...");
        }
    };

    private final ReturnCallback returnCallback =
            (message, replyCode, replyText, exchange, routingKey) ->
                    System.out.println(", replyCode: " + replyCode
                            + ", replyText" + replyText
                            + "return exchange: " + exchange
                            + ", routingKey: " + routingKey);

    /**
     * 发送消息方法调用：构建 Message 消息
     *
     * @param message
     * @param properties
     */
    public void send(Object message, Map<String, Object> properties) {

        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        // id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("1234567890");
        rabbitTemplate.convertAndSend("exchange-1", "springboot.abc", msg, correlationData);
    }
}
