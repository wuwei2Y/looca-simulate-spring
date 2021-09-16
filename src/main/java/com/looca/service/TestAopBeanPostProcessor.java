package com.looca.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;
import java.lang.reflect.Proxy;

/**
 * 模拟Aop
 *
 * @author looca
 */
@Component
public class TestAopBeanPostProcessor implements BeanPostProcessor {

    /**
     * bean初始化后执行
     *
     * @param bean bean
     * @param beanName bean名称
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if("aopService".equals(beanName)) {
            // 生成代理对象
            Object proxyBean = Proxy.newProxyInstance(TestAopBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {

                // 模拟切面逻辑
                System.out.println("此处是模拟切面逻辑");
                // 执行原方法
                return method.invoke(bean);
            });
            return proxyBean;
        }
        return bean;
    }
}
