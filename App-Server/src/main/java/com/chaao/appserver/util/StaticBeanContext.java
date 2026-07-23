package com.chaao.appserver.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class StaticBeanContext implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        StaticBeanContext.beanFactory = factory;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }
}