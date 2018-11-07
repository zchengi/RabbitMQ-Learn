package com.cheng.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cheng
 *         2018/11/7 23:22
 */
public class Producer4TopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException {

        // 1. 创建一个 connectionFactory，并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("cheng");
        connectionFactory.setPassword("zy159357");

        // 2. 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        // 3. 通过 connection 创建 channel
        Channel channel = connection.createChannel();

        // 4. 声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";

        // 5. 发送
        String msg = "Hello World RabbitMQ 4 topic Exchange Message...";
        channel.basicPublish(exchangeName,routingKey1, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, msg.getBytes());

        // 6. 关闭相关的连接
        channel.close();
        connection.close();
    }
}
