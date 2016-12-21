package com.shawntime.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * 数字操作相关的工具类
 * Created by luoxianchao on 2016/11/16.
 */
public final class NumberUtils {

    private NumberUtils() {
        //can't instance
    }

    /**
     * 格式化数字类型成字符串类型
     *
     * @param dou 待格式化的数字
     * @param reg 格式
     * @return 返回格式化后的字符串
     */
    public static String format(Double dou, String reg) {
        Double db = dou;
        if (db == null) {
            db = 0.0;
        }
        String format = reg;
        if (StringUtils.isEmpty(format)) {
            format = "0.00";
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(db);
    }

    /**
     * 转换字符串为double数字类型
     * @param num 待转换字符串
     * @return 转换结果
     */
    public static Double parseDouble(String num){
        String result = num;
        if(StringUtils.isEmpty(result)){
            result = "0";
        }
        return org.apache.commons.lang3.math.NumberUtils.createDouble(result);
    }

    /**
     * 转换字符串为Bigdecimal字类型
     * @param num 待转换字符串
     * @return 转换结果
     */
    public static BigDecimal parseBigDecimal(String num){
        String result = num;
        if(StringUtils.isEmpty(result)){
            result = "0";
        }
        return org.apache.commons.lang3.math.NumberUtils.createBigDecimal(result);
    }

    /**
     * 转换字符串为Integer字类型
     * @param num 待转换字符串
     * @return 转换结果
     */
    public static Integer parseInteger(String num){
        String result = num;
        if(StringUtils.isEmpty(result)){
            result = "0";
        }
        return org.apache.commons.lang3.math.NumberUtils.createInteger(result);
    }

    /**
     * 转换字符串为float字类型
     * @param num 待转换字符串
     * @return 转换结果
     */
    public static Float parseFloat(String num){
        String result = num;
        if(StringUtils.isEmpty(result)){
            result = "0";
        }
        return org.apache.commons.lang3.math.NumberUtils.createFloat(result);
    }
}
