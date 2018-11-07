package com.cheng.api.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * @author cheng
 *         2018/11/7 23:51
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

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

        // 4. 声明（创建）一个队列
        String queueName = "test-001";
        channel.queueDeclare(queueName, true, false, false, null);

        // 5. 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        // 6. 设置 channel
        channel.basicConsume(queueName, true, queueingConsumer);

        // 7. 接收消息
        while (true) {
            Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());

            System.out.println("消费者: " + msg);
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            System.out.println("headers get m1 value: " + headers.get("my1"));
            System.out.println("headers get m2 value: " + headers.get("my2"));

//            Envelope envelope = delivery.getEnvelope();
        }
    }
}
