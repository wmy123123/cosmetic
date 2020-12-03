package com.wmy.cosmetic.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    public static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }
    //根据Bean的名字在工厂中的Bean
    public static Object getBean(String beanName){
        Object bean = context.getBean(beanName);
        return bean;
    }
}
