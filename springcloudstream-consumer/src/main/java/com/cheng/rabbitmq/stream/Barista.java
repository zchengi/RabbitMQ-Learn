package com.cheng.rabbitmq.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 这里的 Barista 接口是定义来作为后面类的参数，这一接口来定义通道类型和通道名称。
 * 通道名称是作为配置用，通道类型则决定了 app 会使用这一通道进行发送消息还是从中接收消息。
 *
 * @author cheng
 *         2018/11/11 23:26
 */
public interface Barista {

    String INPUT_CHANNEL = "input_channel";

    /**
     * 注解 @Input 声明了它是一个输入类型的通道，名字是 Barista.INPUT_CHANNEL，也就是 position3 的 input_channel。
     * 这一名字与上述配置 app2 的配置文件中 position1 应该一致，表明注入了一个名字叫做 input_channel 的通道，
     * 它的类型是 input，订阅的主题是 position2 处声明的 mydest 这个主题
     *
     * @return
     */
    @Input(Barista.INPUT_CHANNEL)
    SubscribableChannel login();
}
