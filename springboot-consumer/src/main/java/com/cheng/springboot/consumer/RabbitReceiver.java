package com.cheng.springboot.consumer;

import com.cheng.springboot.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author cheng
 *         2018/11/6 18:06
 */
@Component
public class RabbitReceiver {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-1", durable = "true"),
                    exchange = @Exchange(name = "exchange-1", durable = "true", type = "topic",
                            ignoreDeclarationExceptions = "true"),
                    key = "springboot.*"
            )
    )

    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {

        System.err.println("--------------------------------------");
        System.err.println("消费端Payload: " + message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.key}")
    )

    @RabbitHandler
    public void onOrderMessage(@Payload Order order,
                               @Headers Map<String, Object> headers,
                               Channel channel) throws Exception {

        // 消费者操作:
        System.out.println("---------收到消息，开始消费---------");
        System.out.println("消费端Order: " + order.getId());

        // 手动确认签收消息
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }
}
