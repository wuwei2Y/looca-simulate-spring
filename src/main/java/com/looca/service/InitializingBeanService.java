package com.looca.service;

import com.spring.InitializingBean;
import com.spring.annotation.Component;

/**
 * 模拟实现InitializingBean接口來自定义初始化方法
 *
 * @author looca
 */
@Component
public class InitializingBeanService implements InitializingBean {

    /**
     * 初始化方法
     */
    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBeanService：初始化方法");
    }
}
