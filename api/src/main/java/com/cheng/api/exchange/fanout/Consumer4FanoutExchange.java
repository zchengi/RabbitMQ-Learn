package com.cheng.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * @author cheng
 *         2018/11/7 23:35
 */
public class Consumer4FanoutExchange {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // 1. 创建一个 connectionFactory，并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("cheng");
        connectionFactory.setPassword("zy159357");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        // 2. 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        // 3. 通过 connection 创建 channel
        Channel channel = connection.createChannel();

        // 4. 声明
        String exchangeName = "test_fanout_exchange";
        String exchangeType = "fanout";
        String queueName = "test_fanout_queue";
        // 不设置路由键
        String routingKey = "";

        // 声明一个交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 声明一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立一个绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        // durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 参数：队列名称、是否自动 ACK、Consumer
        channel.basicConsume(queueName, true, consumer);

        // 循环获取消息
        while (true) {
            // 获取消息，如果没有消息，这一步将一直会阻塞
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息: " + msg);
        }
    }
}
