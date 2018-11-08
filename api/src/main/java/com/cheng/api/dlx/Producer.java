package com.cheng.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cheng
 *         2018/11/8 17:08
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

        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                // 2 表示消息是 持久化的
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                // 过期时间: ms
                .expiration("10000")
                .build();

        String msg = "Hello RabbitMQ DLX Message...";
        channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
    }
}
