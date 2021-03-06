package com.looca.test;

import com.looca.AppConfig;
import com.looca.service.*;
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

        // region 测试单例bean获取结果
//        TestService testService = (TestService) applicationContext.getBean("testService");
//        System.out.println(testService);
//        System.out.println((TestService) applicationContext.getBean("testService"));
//        System.out.println((TestService) applicationContext.getBean("testService"));
        // endregion

        // region 测试原型bean获取结果
//        System.out.println((PrototypeScopeService) applicationContext.getBean("prototypeScopeService"));
//        System.out.println((PrototypeScopeService) applicationContext.getBean("prototypeScopeService"));
//        System.out.println((PrototypeScopeService) applicationContext.getBean("prototypeScopeService"));
        // endregion

        // region 测试@Autowired
//        testService.testAutowired();
        // endregion

        // region 测试aop
//        IAopService aopService = (IAopService) applicationContext.getBean("aopService");
//        aopService.test();
        // endregion

        // region 测试Aware(以BeanNameAware为例)
        TestBeanNameAwareService testBeanNameAwareService =
                (TestBeanNameAwareService) applicationContext.getBean("testBeanNameAwareService");
        testBeanNameAwareService.printBeanName();
        // endregion

    }
}
