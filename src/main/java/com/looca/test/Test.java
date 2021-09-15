package com.looca.test;

import com.looca.AppConfig;
import com.looca.service.TestService;
import com.spring.LoocaApplicationContext;

/**
 * 测试类
 * @author looca
 */
public class Test {

    public static void main(String[] args) {

        /**
         * 1. 扫描bean
         * 2. 创建单例bean(忽略懒加载的情况)
         */
        LoocaApplicationContext applicationContext = new LoocaApplicationContext(AppConfig.class);
        TestService testService = (TestService) applicationContext.getBean("testService");

        testService.test();
    }
}
