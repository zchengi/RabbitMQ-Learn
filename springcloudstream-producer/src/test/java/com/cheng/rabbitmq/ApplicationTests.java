package com.cheng.rabbitmq;

import com.cheng.rabbitmq.stream.RabbitMQSender;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/11/11 23:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Test
    public void sendMessageTest() {

        for (int i = 0; i < 1; i++) {
            try {
                Map<String, Object> properties = new HashMap<>();
                properties.put("SERIAL_NUMBER", "12345");
                properties.put("BANK_NUMBER", "abc");
                properties.put("SERIAL_NUMBER", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

                rabbitMQSender.sendMessage("Hello,I am amqp sender num : " + i, properties);

            } catch (Exception e) {
                System.err.println("---------- error ----------");
                e.printStackTrace();
            }
        }

//        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
