package com.cheng.spring.adapter;

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
}
