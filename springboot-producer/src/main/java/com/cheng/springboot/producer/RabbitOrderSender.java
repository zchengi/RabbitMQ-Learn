package com.cheng.springboot.producer;

import com.cheng.springboot.constant.Constants;
import com.cheng.springboot.entity.Order;
import com.cheng.springboot.mapper.BrokerMessageLogMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;

/**
 * 发送订单消息
 *
 * @author cheng
 *         2018/11/6 17:27
 */
@Component
public class RabbitOrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    private final ConfirmCallback confirmCallback = new ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.out.println("correlationData: " + correlationData);
            String messageId = correlationData.getId();
            if (ack) {
                // 如果 confirm 返回成功，则进行更新
                brokerMessageLogMapper.changeBrokerMessageLogStatus(
                        messageId,
                        Constants.ORDER_SEND_SUCCESS,
                        new Date());
            } else {
                // 失败则进行具体的后续操作：重试或者补偿等手段
                System.out.println("异常处理...");
            }
        }
    };

    /**
     * 发送消息方法调用: 构建自定义对象消息
     *
     * @param order
     */
    public void send(Order order) {

        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(order.getMessageId());

        // exchange routingKey 消息体内容
        rabbitTemplate.convertAndSend("order-exchange",
// 测试消息发送失败情况
//        rabbitTemplate.convertAndSend("order-exchange11",
                "order.test",
                order,
                correlationData);
    }
}
