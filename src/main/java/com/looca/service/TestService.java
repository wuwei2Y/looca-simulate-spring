package com.looca.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;

/**
 * 测试用例service
 * @author looca
 */
@Component("testService")
public class TestService {

    @Autowired
    private AutowiredService autowiredService;

    public void test() {
        System.out.println("Test");
    }

    public void testAutowired() {
        System.out.println(autowiredService);
    }


}
