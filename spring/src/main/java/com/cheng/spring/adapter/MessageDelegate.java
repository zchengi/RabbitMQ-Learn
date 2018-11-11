package com.cheng.spring.adapter;

import com.cheng.spring.entity.Order;
import com.cheng.spring.entity.Packaged;

import java.io.File;
import java.util.Map;

/**
 * @author cheng
 *         2018/11/9 14:27
 */
public class MessageDelegate {

    public void handleMessage(byte[] messageBody) {
        System.out.println("默认方法 收到消息内容：" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.out.println("字节数组方 收到消息内容：" + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.out.println("字符串方法 收到消息内容: " + messageBody);
    }

    public void method1(String messageBody) {
        System.out.println("method1 收到消息内容: " + messageBody);
    }

    public void method2(String messageBody) {
        System.out.println("method2 收到消息内容: " + messageBody);
    }

    public void consumeMessage(Map messageBody) {
        System.out.println("map方法 收到消息内容: " + messageBody);
    }

    public void consumeMessage(Order order) {
        System.out.println("order对象 收到消息内容, id: " + order.getId()
                + ", name: " + order.getName()
                + ", content: " + order.getContent());
    }

    public void consumeMessage(Packaged packaged) {
        System.out.println("package对象 收到消息内容, id: " + packaged.getId()
                + ", name: " + packaged.getName()
                + ", content: " + packaged.getDescription());
    }

    public void consumeMessage(File file) {
        System.out.println("文件对象 收到消息内容: " + file.getName());
    }
}
