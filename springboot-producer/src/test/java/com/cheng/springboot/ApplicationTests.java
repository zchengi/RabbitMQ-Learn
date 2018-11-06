package com.cheng.springboot;

import com.cheng.springboot.entity.Order;
import com.cheng.springboot.producer.OrderSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private OrderSender orderSender;

    @Test
    public void testSend1() throws Exception {

        Order order = new Order("201811061752", "测试订单1",
                System.currentTimeMillis() + "$" + UUID.randomUUID());

        orderSender.send(order);
    }
}
