package com.looca.service;

import com.spring.annotation.Component;
import com.spring.aware.BeanNameAware;

/**
 * 测试模拟BeanNameAware
 *
 * @author looca
 */
@Component
public class TestBeanNameAwareService implements BeanNameAware {

    private String beanName;

    public void printBeanName() {
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
