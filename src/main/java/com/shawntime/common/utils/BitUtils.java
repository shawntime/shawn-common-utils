package com.shawntime.common.utils;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-24 9:21
 * Desc: 位运算方法工具类整理
 */
public final class BitUtils {

    private BitUtils() {
        //
    }

    /**
     *  获取当前index状态，［0,1］；
     *  @param value     存储的开关数据int32
     *  @param index     第几位
     *  @return          当前位置的值
     */
    public static boolean getStatus(int value, int index){
        return ( value >> (index - 1) & 1 ) == 1;
    }

    /**
     *  处理后的存储数据 int32
     *  @param value     存储的开关数据int32
     *  @param index     第几位
     *  @param set       开关状态
     *  @return          当前位置的值［0,1］
     */
    public static int addStatus(int value,int index, boolean set){
        index --;
        if(set){
            value = 1 << index | value;
        } else {
            value = ~(1<<index) & value;
        }
        return value;
    }
}
