package com.spring;

/**
 * 模拟BeanPostProcessor
 *
 * @author looca
 */
public interface BeanPostProcessor {

    /**
     * 初始化前执行方法
     *
     * @param bean bean
     * @param beanName bean名称
     * @return
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后执行方法
     * @param bean bean
     * @param beanName bean名称
     * @return
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }


}
