package com.shawntime.common.web;

import com.shawntime.common.utils.TelUtils;
import com.shawntime.common.web.annotation.AutoValidate;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by YinZhaohua on 16/4/11.
 */
public class BaseDTO {

    private static Logger logger = Logger.getLogger(BaseDTO.class);

    /**
     * 校验返回提示内容
     */
    private String returnMsg;

    /**
     * 校验返回错误状态码
     */
    private Integer returnCode = -1;

    /**
     * 校验方法,需要在校验处手动调用
     *
     * @return
     */
    public boolean validate() {

        Class thisClass = this.getClass();
        Field[] fields = thisClass.getDeclaredFields();
        try {
            for (Field f : fields) {
                AutoValidate autoValidate = f.getAnnotation(AutoValidate.class);
                if (autoValidate == null) {
                    continue;
                }
                Method m = thisClass.getMethod(functionNameForGet(f.getName()));
                Object obj = m.invoke(this);

                if (autoValidate.required()) {
                    if (obj == null || obj.toString().length() <= 0) {
                        returnMsg = autoValidate.prefixMsg() + Constants.REQUIRED_MSG;
                        return false;
                    }
                }

                if (autoValidate.maxLength() != -1) {
                    int maxLength = autoValidate.maxLength();
                    if (obj != null && String.valueOf(obj).length() > maxLength) {
                        returnMsg = String.format(autoValidate.prefixMsg() + Constants.MAXLENGTH_MSG, maxLength);
                        return false;
                    }
                }

                if (autoValidate.phone()) {
                    if (obj != null && !TelUtils.isMayBeMobiePhoneNum(String.valueOf(obj))) {
                        returnMsg = String.format(autoValidate.prefixMsg() + Constants.PHONE_MSG);
                        return false;
                    }
                }

            }
        } catch (Exception e) {
            returnMsg = "未知的错误";
            logger.error(returnMsg + e.getMessage(), e);
            return false;
        }
        return true;

    }

    /*  私有方法,获取get方法  */
    private String functionNameForGet(String fieldName) {
        char[] array = fieldName.toCharArray();
        if ('a' <= array[0] && array[0] <= 'z') {
            array[0] -= 32;
        }
        return "get" + String.valueOf(array);
    }

    /**
     * 获得错误协议体
     */
    public Protocol resturnProtocol() {
        Protocol protocol = new Protocol();
        protocol.setReturncode(returnCode);
        protocol.setMessage(returnMsg);
        protocol.setResult(new Object());
        return protocol;
    }
}
