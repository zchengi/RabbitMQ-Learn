package com.cheng.api.returnlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cheng
 *         2018/11/8 15:37
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("cheng");
        connectionFactory.setPassword("zy159357");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        // 监控不可达的消息
        channel.addReturnListener((replyCode, replyText, exchange, routingKey1, properties, body) -> {
            System.out.println("---------- handle return ----------");
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey1: " + routingKey1);
            System.out.println("properties: " + properties);
            System.out.println("body: " + new String(body));
        });

        String msg = "Hello RabbitMQ Return Message...";
//        channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKeyError, /*false*/true, null, msg.getBytes());
    }
}
