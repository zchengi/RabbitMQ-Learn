package com.cheng.rabbitmq.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author cheng
 *         2018/11/11 23:08
 */
@EnableBinding(Barista.class)
@Service
public class RabbitMQSender {

    @Autowired
    private Barista barista;

    public String sendMessage(Object message, Map<String, Object> properties) {

        try {

            MessageHeaders messageHeaders = new MessageHeaders(properties);
            Message msg = MessageBuilder.createMessage(message, messageHeaders);
            boolean sendStatus = barista.logout().send(msg);

            System.out.println("---------- sending ----------");
            System.out.println("发送数据：" + message + "，sendStatus：" + sendStatus);

        } catch (Exception e) {
            System.err.println("---------- error ----------");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }
}
