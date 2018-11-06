package com.cheng.springboot.service;

import com.cheng.springboot.constant.Constants;
import com.cheng.springboot.entity.BrokerMessageLog;
import com.cheng.springboot.entity.Order;
import com.cheng.springboot.mapper.BrokerMessageLogMapper;
import com.cheng.springboot.mapper.OrderMapper;
import com.cheng.springboot.producer.RabbitOrderSender;
import com.cheng.springboot.util.FastJsonConvertUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cheng
 *         2018/11/7 0:39
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    public void createOrder(Order order) {

        // order current time
        Date orderTime = new Date();
        // order insert  业务数据入库
        orderMapper.insert(order);

        // log insert 构建消息日志记录对象
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        brokerMessageLog.setMessageId(order.getMessageId());

        // save order message as json
        brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
        // 设置订单发送状态为：0，表示发送中
        brokerMessageLog.setStatus(Constants.ORDER_SENDING);
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());

        brokerMessageLogMapper.insert(brokerMessageLog);

        // order message sender
        rabbitOrderSender.send(order);
    }
}
