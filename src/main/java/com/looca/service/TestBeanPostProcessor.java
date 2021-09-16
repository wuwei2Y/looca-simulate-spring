package com.looca.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

/**
 * 模拟自定义Bean后置处理器
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor {

    /**
     * bean初始化前执行方法
     *
     * @param bean bean
     * @param beanName bean名称
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("TestBeanPostProcessorService:bean初始化前执行方法");
        return bean;
    }

    /**
     * bean初始化后执行方法
     *
     * @param bean bean
     * @param beanName bean名称
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("TestBeanPostProcessorService:bean初始化后执行方法");
        return bean;
    }
}
