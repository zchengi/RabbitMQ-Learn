package com.cheng.api.confirmlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * @author cheng
 *         2018/11/8 15:09
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // 1. 创建 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("cheng");
        connectionFactory.setPassword("zy159357");

        // 2. 获取 connection
        Connection connection = connectionFactory.newConnection();

        // 3. 通过 connection 创建一个新的 channel
        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.#";
        String queueName = "test_confirm_queue";

        // 4. 声明交换机和队列，然后进行绑定设置，指定路由 key
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 5. 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println(msg);
        }
    }
}
