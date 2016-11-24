package com.shawntime.common.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * JavaBean操作类
 */
public final class BeanUtils {

    public static final String FiledClassName = "class";

    private BeanUtils() {
    }

    public static Map beanToMap(Object object, String dateFormatPattern) {
        return beanToMap(object, null, dateFormatPattern);
    }

    public static Map beanToMap(Object object) {
        return beanToMap(object, null, "yyyy-MM-dd HH:mm:ss");
    }

    public static Map beanToMap(Object object, String[] fieldNameArray, String dateFormatPattern) {
        Map map = null;
        try {
            map = convertToMap(object, dateFormatPattern);
            deleteFiled(fieldNameArray, map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private static Map<String, Object> convertToMap(Object object) {
        return convertToMap(object, null);
    }

    private static Map<String, Object> convertToMap(Object object, String dateFormatPattern) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<>();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldObject = field.get(object);
                if (fieldObject != null) {
                    if (field.getType() == Date.class) {
                        if (StringUtils.isNotEmpty(dateFormatPattern)) {
                            String dateString = new SimpleDateFormat(dateFormatPattern, Locale.CHINA).format(fieldObject);
                            map.put(field.getName(), dateString);
                        }
                    } else if (field.getType() == Integer.class || field.getType() == Long.class || field.getType() == Double.class || field.getType() == BigDecimal.class) {
                        String numberString = fieldObject.toString();
                        map.put(field.getName(), numberString);
                    } else {
                        map.put(field.getName(), fieldObject);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private static void deleteFiled(String[] fieldNameArray, Map map) {
        if (fieldNameArray != null && fieldNameArray.length > 0) {
            deleteFiledClass(map);
            for (String fieldName : fieldNameArray) {
                map.remove(fieldName);
            }
        }
    }

    private static void deleteFiledClass(Map map) {
        if (map != null && map.keySet().size() > 0) {
            map.remove(FiledClassName);
        }
    }


}
