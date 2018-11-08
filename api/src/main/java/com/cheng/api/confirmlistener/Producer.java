package com.cheng.api.confirmlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author cheng
 *         2018/11/8 15:09
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {

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

        // 4. 指定消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        // 5. 发送一条消息
        String msg = "Hello RabbitMQ Send confirm message";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        // 6. 添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) {
                System.err.println("------------- ack! -------------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                System.err.println("-----------  no ack! -----------");
            }
        });
    }
}
