package com.shawntime.common.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by zhouxiaoming on 2015/9/4.
 * 利用fastjson序列化和反序列化
 */
public final class JsonUtils {

    final static String defaultDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private JsonUtils() {
    }

    public static <T> String serialize(T t) {
        JSON.DEFFAULT_DATE_FORMAT = defaultDateFormat;
        return serialize(t, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    public static <T> String serialize(T t, SerializerFeature... features) {
        return JSON.toJSONString(t, features);
    }

    public static <T> T deSerialize(String string, Class<T> tClass) {
        return JSON.parseObject(string, tClass);
    }

    public static <T> List<T> deSerializeList(String string, Class<T> tClass) {
        return JSON.parseArray(string, tClass);
    }

    public static <T> T complexJsonToObject(String jsonString) {
        return JSON.parseObject(jsonString, new TypeReference<T>() {
        }.getType());
    }

}
