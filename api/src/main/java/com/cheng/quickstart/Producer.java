package com.cheng.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产端
 *
 * @author cheng
 *         2018/11/7 17:54
 */
public class Producer {
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

        // 4. 通过 channel 发送数据
        String message = "Hello RabbitMQ";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("", "test-001", null, message.getBytes());
        }

        // 5. 关闭相关的连接
        channel.close();
        connection.close();
    }
}
