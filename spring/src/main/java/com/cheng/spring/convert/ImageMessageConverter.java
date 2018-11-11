package com.cheng.spring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @author cheng
 *         2018/11/11 21:59
 */
public class ImageMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException("convert error!");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        System.out.println("---------- Image MessageConverter ----------");

        Object _extName = message.getMessageProperties().getHeaders().get("extName");
        String extName = _extName == null ? "png" : _extName.toString();

        byte[] body = message.getBody();
        String fileName = UUID.randomUUID().toString();
        String path = "d:/" + fileName + "." + extName;
        File file = new File(path);

        try {
            Files.copy(new ByteArrayInputStream(body), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
