package com.cheng.springboot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * FastJson 工具类
 *
 * @author cheng
 *         2018/11/6 23:17
 */
public class FastJsonConvertUtil {

    private static final SerializerFeature[] FEATURES_WITH_NULL_VALUE = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty};


    private FastJsonConvertUtil() {}

    /**
     * 将JSON字符串转换为实体对象
     *
     * @param data  JSON字符串
     * @param clazz 转换对象
     * @param <T>
     * @return
     */
    public static <T> T convertJSONToObject(String data, Class<T> clazz) {
        try {
            return JSON.parseObject(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSONObject对象转换为实体对象
     *
     * @param data  JSONObject对象
     * @param clazz 转换对象
     * @param <T>
     * @return
     */
    public static <T> T convertJSONToObject(JSONObject data, Class<T> clazz) {
        try {
            return JSONObject.toJavaObject(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON字符串数组转为List集合对象
     *
     * @param data  JSON字符串数组
     * @param clazz 转换对象
     * @param <T>   集合对象
     * @return
     */
    public static <T> List<T> convertJSONToArray(String data, Class<T> clazz) {
        try {
            return JSON.parseArray(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON字符串数组转为List集合对象
     *
     * @param data  List<JSONObject>
     * @param clazz 转换对象
     * @param <T>   集合对象
     * @return
     */
    public static <T> List<T> convertJSONToArray(List<JSONObject> data, Class<T> clazz) {
        try {
            List<T> t = new ArrayList<>();
            for (JSONObject jsonObject : data) {
                t.add(convertJSONToObject(jsonObject, clazz));
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object 任意对象
     * @return JSON字符串
     */
    public static String convertObjectToJSON(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象转为JSONObject对象
     *
     * @param object 任意对象
     * @return JSONObject对象
     */
    public static String convertObjectToJSONObject(Object object) {
        try {
            return (String) JSONObject.toJSON(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用空值特性将对象转换为JSON
     *
     * @param object 任意对象
     * @return JSON字符串
     */
    public static String convertObjectToJSONWithNullValue(Object object) {
        try {
            return JSON.toJSONString(object, FEATURES_WITH_NULL_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
