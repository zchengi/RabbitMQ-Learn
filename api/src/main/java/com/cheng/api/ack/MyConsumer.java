package com.cheng.api.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import static com.rabbitmq.client.AMQP.BasicProperties;

/**
 * @author cheng
 *         2018/11/8 16:35
 */
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
            throws IOException {

        System.out.println("---------- consumer message ----------");
        System.out.println("body: " + new String(body));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer num = (Integer) properties.getHeaders().get("num");
        if (num == 0) {
            // requeue: 是否重回队列
//            channel.basicNack(envelope.getDeliveryTag(), false, true);
            channel.basicNack(envelope.getDeliveryTag(), false, false);
        } else {
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
