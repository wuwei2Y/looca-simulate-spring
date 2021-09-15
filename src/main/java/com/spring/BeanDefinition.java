package com.spring;

/**
 * 模拟BeanDefinition，用来封装bean的所有属性
 */
public class BeanDefinition {



    /**
     * bean对应的class
     */
    private Class type;
    /**
     * bean的作用域
     */
    private String scope;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
