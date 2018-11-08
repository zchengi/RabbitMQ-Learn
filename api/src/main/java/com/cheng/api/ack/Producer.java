package com.cheng.api.ack;

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
 *         2018/11/8 16:35
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

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.save";


        for (int i = 0; i < 5; i++) {
            Map<String, Object> headers = new HashMap<>(16);
            headers.put("num", i);
            BasicProperties properties = new BasicProperties().builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .headers(headers).build();

            String msg = "Hello RabbitMQ ACK Message... " + i;
            channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
        }
    }
}
