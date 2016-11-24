package com.shawntime.common.config;

import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class PropertyConfigurer {

    private static Logger logger = Logger.getLogger(PropertyConfigurer.class);

    public static Properties props = null;

    public static void load(StringReader reader) {
        if (null == props)
            props = new Properties();
        try {
            props.load(reader);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void load(Properties defaultProps) {
        if (null == props) {
            props = new Properties();
            convertProperties(defaultProps);
        } else {
            convertProperties(defaultProps);
        }
    }

    public static void convertProperties(Properties defaultProps) {
        Enumeration<?> propertyNames = defaultProps.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = (String) propertyNames.nextElement();
            String propertyValue = defaultProps.getProperty(propertyName);
            if (StringUtils.isNotBlank(propertyName)) {
                props.setProperty(propertyName, propertyValue);
            }
        }
    }

    public static Object getProperty(String key) {
        if (null == props)
            return null;
        else
            return props.get(key);
    }

    public static String getValue(String key) {
        Object object = getProperty(key);
        if (null != object) {
            return (String) object;
        } else {
            logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
            return null;
        }
    }

    public static String getValue(String key, String defaultValue) {
        Object object = getProperty(key);
        if (null != object) {
            return (String) object;
        } else {
            logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
            return defaultValue;
        }
    }

    public static String getString(String key) {
        Object object = getProperty(key);
        if (null != object) {
            return (String) object;
        } else {
            logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
            return null;
        }
    }

    public static String getString(String key, String defaultString) {
        Object object = getProperty(key);
        if (null != object) {
            return (String) object;
        } else {
            logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
            return defaultString;
        }
    }

    public static Long getLong(String key) {
        try {
            Object object = getProperty(key);
            if (null != object)
                return Long.parseLong(object.toString());
            else {
                logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Long getLong(String key, long defaultLong) {
        try {
            Object object = getProperty(key);
            if (null != object)
                return Long.parseLong(object.toString());
            else {
                logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
                return defaultLong;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Integer getInteger(String key) {
        try {
            Object object = getProperty(key);
            if (null != object) {
                return Integer.parseInt(object.toString());
            } else {
                logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static Integer getInteger(String key, int defaultInt) {
        try {
            Object object = getProperty(key);
            if (null != object) {
                return Integer.parseInt(object.toString());
            } else {
                logger.warn("配置项为" + key + "的配置未添加或设置的内容为空");
                return defaultInt;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String getValue(String key, Object... array) {
        try {
            String message = getValue(key);
            if (null != message) {
                return MessageFormat.format(message, array);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}