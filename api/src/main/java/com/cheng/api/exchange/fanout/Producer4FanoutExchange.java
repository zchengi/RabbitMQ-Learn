package com.cheng.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cheng
 *         2018/11/7 23:35
 */
public class Producer4FanoutExchange {
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
        String exchangeName = "test_fanout_exchange";

        // 5. 发送
        String msg = "Hello World RabbitMQ 4 fanout Exchange Message...";
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(exchangeName, "qwer", null, msg.getBytes());
        }

        // 6. 关闭相关的连接
        channel.close();
        connection.close();
    }
}
