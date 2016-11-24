package com.shawntime.common.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Bean工厂后置处理器的实现,BeanFactoryPostProcessor接口的一个实现
 * 配置工具
 *
 * @author YinZhaohua
 */
public class ExtendedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
        PropertyConfigurer.load(props);
    }

}