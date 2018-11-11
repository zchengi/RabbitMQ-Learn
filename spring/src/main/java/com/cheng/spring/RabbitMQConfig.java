package com.cheng.spring;

import com.cheng.spring.adapter.MessageDelegate;
import com.cheng.spring.convert.ImageMessageConverter;
import com.cheng.spring.convert.PDFMessageConverter;
import com.cheng.spring.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author cheng
 *         2018/11/9 11:56
 */
@Configuration
@ComponentScan({"com.cheng.spring.*"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses("192.168.0.103:5672");
        cachingConnectionFactory.setUsername("cheng");
        cachingConnectionFactory.setPassword("zy159357");
        cachingConnectionFactory.setVirtualHost("/");

        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.isAutoStartup();

        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue001(), queue002(), queue003(), queueImage(), queuePdf());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);

        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID());
//        container.setMessageListener(message -> System.out.println("消费者：" + new String(message.getBody())));

        // 消息监听适配器

        // 1. 适配器方式
        // 默认有自己的方法：handleMessage
        // 也可以自己指定一个方法名字：consumeMessage
        // 也可以添加一个转换器：从字节数组转换为 String (发送的消息的 ContentType 必须包含 'text')
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);*/

        // 2. 适配器方式
        // 队列名称和方法名称也可以进行一一匹配
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());

        Map<String, String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put("queue001", "method1");
        queueOrTagToMethodName.put("queue002", "method2");
        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        adapter.setMessageConverter(new TextMessageConverter());

        container.setMessageListener(adapter);*/


        // 1.1 支持 json 格式的转换器
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);

        container.setMessageListener(adapter);*/

        // 1.2 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换（默认只支持 util lang 包转换）
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        /*DefaultJackson2JavaTypeMapper javaTypeMapper = new RabbitMqJsonMessageClassMapper();

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        adapter.setMessageConverter(jackson2JsonMessageConverter);

        container.setMessageListener(adapter);*/

        // 1.3 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
        /*DefaultJackson2JavaTypeMapper javaTypeMapper = new RabbitMqJsonMessageClassMapper();

        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("order", com.cheng.spring.entity.Order.class);
        idClassMapping.put("packaged", com.cheng.spring.entity.Packaged.class);
        javaTypeMapper.setIdClassMapping(idClassMapping);

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        adapter.setMessageConverter(jackson2JsonMessageConverter);

        container.setMessageListener(adapter);*/

        // 1.4 ext convert
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        // 全局的转换器:
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textMessageConverter = new TextMessageConverter();
        converter.addDelegate("text", textMessageConverter);
        converter.addDelegate("html/text", textMessageConverter);
        converter.addDelegate("text/xml", textMessageConverter);
        converter.addDelegate("text/plain", textMessageConverter);

        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json", jsonMessageConverter);
        converter.addDelegate("application/json", jsonMessageConverter);

        ImageMessageConverter imageMessageConverter = new ImageMessageConverter();
        converter.addDelegate("image", imageMessageConverter);
        converter.addDelegate("image/png", imageMessageConverter);

        PDFMessageConverter pdfMessageConverter = new PDFMessageConverter();
        converter.addDelegate("application/pdf", pdfMessageConverter);

        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);

        return container;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无 routingKey 的概念
     * HeadersExchange：通过添加属性 key-value 匹配
     * DirectExchange：按照 routingKey 分发到指定队列
     * TopicExchange：多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        // 队列持久
        return new Queue("queue001", true);
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        // 队列持久
        return new Queue("queue002", true);
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        // 队列持久
        return new Queue("queue003", true);
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public Queue queueImage() {
        // 队列持久
        return new Queue("image_queue", true);
    }

    @Bean
    public Queue queuePdf() {
        // 队列持久
        return new Queue("pdf_queue", true);
    }
}
