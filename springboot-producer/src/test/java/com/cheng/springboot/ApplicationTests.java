package com.cheng.springboot;

import com.cheng.springboot.entity.Order;
import com.cheng.springboot.producer.RabbitOrderSender;
import com.cheng.springboot.service.OrderService;
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
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private OrderService orderService;

    @Test
    public void SendTest() {

        Order order = new Order("201811061752", "测试订单1",
                System.currentTimeMillis() + "$" + UUID.randomUUID());

        rabbitOrderSender.send(order);
    }

    @Test
    public void CreateOrderTest() {

        Order order = new Order("201811070119", "测试创建订单",
                System.currentTimeMillis() + "$" + UUID.randomUUID());
        orderService.createOrder(order);
    }
}