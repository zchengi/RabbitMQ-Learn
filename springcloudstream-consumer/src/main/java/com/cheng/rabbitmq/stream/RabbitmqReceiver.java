package com.cheng.rabbitmq.stream;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author cheng
 *         2018/11/11 23:26
 */
@EnableBinding(Barista.class)
@Service
public class RabbitmqReceiver {

    @StreamListener(Barista.INPUT_CHANNEL)
    public void receiver(Message message) throws IOException {

        Channel channel = (Channel) message.getHeaders().get(AmqpHeaders.CHANNEL);
        Long deliverTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        System.out.println("Input Stream 1 接收数据：" + message);
        System.out.println("---------- 消费完毕 ----------");

        channel.basicAck(deliverTag, false);
    }
}
