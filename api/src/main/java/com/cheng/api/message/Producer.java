package com.cheng.api.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.rabbitmq.client.AMQP.BasicProperties;

/**
 * @author cheng
 *         2018/11/7 23:51
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

        Map<String, Object> headers = new HashMap<>();
        headers.put("my1", "111");
        headers.put("my2", "222");

        BasicProperties properties = new BasicProperties().builder()
                // 2 表示消息是 持久化的
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                // 过期时间: ms
                .expiration("10000")
                // 自定义属性
                .headers(headers)
                .build();

        // 4. 通过 channel 发送数据
        String message = "Hello RabbitMQ";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("", "test-001", properties, message.getBytes());
        }

        // 5. 关闭相关的连接
        channel.close();
        connection.close();
    }
}
