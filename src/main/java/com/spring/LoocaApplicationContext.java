package com.spring;

/**
 * 模拟Spring中的ApplicationContext（以AnnotationConfigApplicationContext为例）
 * @author looca
 */
public class LoocaApplicationContext {

    /**
     * 配置类
     */
    private Class configClass;

    /**
     * 构造参数
     * @param configClass 外部传入的配置类
     */
    public LoocaApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    /**
     * 模拟获取bean
     * @param beanName bean名称
     * @return bean实例
     */
    public Object getBean(String beanName) {
        // 后续补充
        return null;
    }
}
