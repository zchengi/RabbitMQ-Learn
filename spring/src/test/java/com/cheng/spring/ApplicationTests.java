package com.cheng.spring;

import com.cheng.spring.entity.Order;
import com.cheng.spring.entity.Packaged;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void adminTest() {

        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        rabbitAdmin.declareBinding(new Binding("test.direct.queue",
                Binding.DestinationType.QUEUE, "test.direct",
                "direct", new HashMap<>()));
        rabbitAdmin.declareBinding(BindingBuilder
                // 直接创建队列
                .bind(new Queue("test.topic.queue", false))
                // 直接创建交换机，建立连接关系
                .to(new TopicExchange("test.topic", false, false))
                // 指定路由 key
                .with("user.#"));

        rabbitAdmin.declareBinding(BindingBuilder
                // 直接创建队列
                .bind(new Queue("test.fanout.queue", false))
                // 直接创建交换机，建立连接关系
                .to(new FanoutExchange("test.fanout", false, false)));


        // 清空队列
        rabbitAdmin.purgeQueue("test.topic.queue", false);
    }

    @Test
    public void sendMessageTest() {

        // 1. 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "消息描述...");
        messageProperties.getHeaders().put("type", "自定义消息类型...");

        Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);

        // 2. 发送消息
        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message,
                message1 -> {
                    System.out.println("---------- 添加额外设置 ----------");
                    message.getMessageProperties().getHeaders().put("desc", "额外修改了哈");
                    message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
                    return message;
                });
    }

    @Test
    public void sendMessageTest2() {

        // 1. 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("MQ Message...".getBytes(), messageProperties);

        // 2. 发送消息
        rabbitTemplate.convertAndSend("topic001", "spring.amqp", "Hello Object Message Send...");
        rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "Hello Object Message Send...");

        rabbitTemplate.send("topic001", "spring.acb", message);
    }

    @Test
    public void SendMessage4TextTest() {

        // 1. 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");

        Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.abc", message);
        rabbitTemplate.send("topic002", "rabbit.abc", message);
    }

    @Test
    public void sendJsonMessageTest() throws JsonProcessingException {

        Order order = new Order("001", "消息订单", "描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println("order 4 json " + json);

        MessageProperties messageProperties = new MessageProperties();
        // 这里一定要修改 contentType 为 application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }

    @Test
    public void sendJavaMessageTest() throws JsonProcessingException {

        Order order = new Order("001", "消息订单", "描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println("order 4 json " + json);

        MessageProperties messageProperties = new MessageProperties();
        // 这里一定要修改 contentType 为 application/json
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "com.cheng.spring.entity.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }

    @Test
    public void sendMappingMessageTest() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        Order order = new Order("001", "消息订单", "描述信息");
        String json1 = mapper.writeValueAsString(order);
        System.out.println("order 4 json " + json1);

        MessageProperties messageProperties = new MessageProperties();
        // 这里一定要修改 contentType 为 application/json
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "order");
        Message message = new Message(json1.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "spring.order", message);


        Packaged packaged = new Packaged("002", "包裹信息", "包裹描述信息");
        String json2 = mapper.writeValueAsString(packaged);
        System.out.println("packaged 4 json: " + json2);

        MessageProperties messageProperties2 = new MessageProperties();
        // 这里一定要修改 contentType 为 application/json
        messageProperties2.setContentType("application/json");
        messageProperties2.getHeaders().put("__TypeId__", "packaged");
        Message message2 = new Message(json2.getBytes(), messageProperties2);
        rabbitTemplate.send("topic001", "spring.packaged", message2);
    }

    @Test
    public void sendExtConverterMessageTest() throws IOException {

        // image
        /*byte[] body = Files.readAllBytes(Paths.get("C:/", "picture.png"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("image/png");
        messageProperties.getHeaders().put("extName", "png");
        Message message = new Message(body, messageProperties);
        // 如果 exchanged 为空, routingKey 匹配 queue 的名称
        rabbitTemplate.send("", "image_queue", message);*/

        // pdf
        byte[] body = Files.readAllBytes(Paths.get("C:/", "223.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(body, messageProperties);
        // 如果 exchanged 为空, routingKey 匹配 queue 的名称
        rabbitTemplate.send("", "pdf_queue", message);
    }
}
