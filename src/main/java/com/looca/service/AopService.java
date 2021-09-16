package com.looca.service;

import com.spring.annotation.Component;

/**
 * 模拟AOP
 * 通过JDK动态代理来生成代理类
 */
@Component
public class AopService implements IAopService {
    @Override
    public void test() {
        System.out.println(this);
    }
}
