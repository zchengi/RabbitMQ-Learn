package com.cheng.springboot.task;

import com.cheng.springboot.constant.Constants;
import com.cheng.springboot.entity.BrokerMessageLog;
import com.cheng.springboot.entity.Order;
import com.cheng.springboot.mapper.BrokerMessageLogMapper;
import com.cheng.springboot.producer.RabbitOrderSender;
import com.cheng.springboot.util.FastJsonConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/11/7 0:46
 */
@Component
public class RetryMessageTask {

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 3000, fixedDelay = 10000)
    public void reSend() {
        System.out.println("------------ 定时任务开始 ------------");

        // pull status = 0 and timeout message
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        list.forEach(messageLog -> {
            if (messageLog.getTryCount() >= 3) {
                // update fail message
                brokerMessageLogMapper.changeBrokerMessageLogStatus(
                        messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
            } else {
                // resend
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(), new Date());
                Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
                try {
                    rabbitOrderSender.send(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("------------- 异常处理 -------------");
                }
            }
        });
    }
}
