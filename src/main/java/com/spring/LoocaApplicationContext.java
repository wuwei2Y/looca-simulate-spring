package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;
import com.spring.tool.CommonConstant;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 用来存放BeanDefinition的map
     * key为beanName，value为BeanDefinition
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    /**
     * 用来存放单例bean的map，也就是单例池
     * key为beanName, value为bean实例
     */
    private Map<String, Object> singletonObjects = new HashMap<>();
    /**
     * 用来存放实现了BeanPostProcessor的列表，此处使用ArrayList来代替LinkedList
     */
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * 构造参数
     * @param configClass 外部传入的配置类
     */
    public LoocaApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描bean
        doScan(configClass);

        // 遍历beanDefinitionMap并创建单例bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();

            if (CommonConstant.SCOPE_SINGLETON.equals(beanDefinition.getScope())) {
                // 单例bean
                Object singletonBean = doCreateBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
        }
    }

    /**
     * 模拟创建bean
     * 1. 根据bean定义获取bean对应的.class
     * 2. 经过推断构造器后选用合适的构造器来创建类(此处不在进行推断，仅使用无参构造器来创建类)
     * 3. 属性填充
     * 4. 初始化
     *
     * @param beanName bean名称
     * @param beanDefinition bean定义
     * @return bean实例
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {

        // 获取类对应的class
        Class clazz = beanDefinition.getType();
        // 使用无参构造器来创建类
        try {
            Object instance = clazz.getConstructor().newInstance();

            // 属性填充
            for (Field field : clazz.getDeclaredFields()) {

                // 如果属性有@Autowired注解修饰则进行自动装配
                if (field.isAnnotationPresent(Autowired.class)) {

                    field.setAccessible(true);
                    // 此处为了简化直接根据name来找对应的bean来进行自动装配
                    field.set(instance, getBean(field.getName()));

                }
            }

            // 初始化前
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化
            if(instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // 初始化后
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 模拟获取bean
     * 1. 判断beanName对应的bean定义是否存在
     * 2. 判断需要获取的bean是单例还是原型的
     * 3. 单例bean从单例池中直接获取
     * 4. 原型bean则重新创建一个bean实例
     *
     * @param beanName bean名称
     * @return bean实例
     */
    public Object getBean(String beanName) {

        // 判断是否有beanName对应的beanDefinition
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException("没有找到对应的bean");
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(CommonConstant.SCOPE_SINGLETON.equals(beanDefinition.getScope())) {
            // 如果是单例的则直接从单例池中获取bean实例
            Object singletonBean = singletonObjects.get(beanName);
            if(singletonBean == null) {
                singletonBean = doCreateBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            // 如果是多例的则重新创建bean实例
            Object prototypeBean = doCreateBean(beanName, beanDefinition);
            return prototypeBean;
        }
    }

    /**
     * 模拟扫描bean
     * 1. 判断传入的配置类是否带有@ComponentScan注解
     * 2. 获取@ComponentScan注解配置的扫描路径
     * 3. 获取扫描路径下所有的class文件并遍历
     * 4. 加载bean判断bean是否有@Component注解
     * 5. 处理含有@Component注解的bean，创建对应的beanDefinition
     * 6. 判断bean是单例还是原型，设置beanDefinition，存入beanDefinitionMap
     *
     * @param configClass 配置类
     */
    private void doScan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {

            // 获取@ComponentScan
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            // 获取@ComponentScan配置的扫描路径(e.g: com.looca.service)
            String scanPath = componentScanAnnotation.value();
            // 将路径转换为文件系统路径(e.g com/looca/service)
            scanPath = scanPath.replace(".", "/");

            /**
             * 前面获取的扫描路径是项目路径，真正应该扫描的应该是.class文件的路径
             * .class文件是由AppClassLoader来加载的，因此此处先获取AppClassLoader
             */
            ClassLoader appClassLoader = LoocaApplicationContext.class.getClassLoader();
            // 获取需要扫描的目录
            URL resource = appClassLoader.getResource(scanPath);
            File dir = new File(resource.getFile());

            // 判断获取的是否为目录
            if (dir.isDirectory()) {
                // 如果是则遍历目录中的文件
                for(File file : dir.listFiles()) {

                    /**
                     * 判断文件是否有@Component注解
                     * 先使用类加载器加载文件对应的类(需要将文件路径转换成项目类路径
                     *   e.g: x:xxx/target/class/com/looca/service/TestService.class -> com.looca.service.TestService)
                     */
                    // 获取文件对应的类路径
                    String absolutePath = file.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.lastIndexOf("com"), absolutePath.lastIndexOf(".class"));
                    absolutePath = absolutePath.replace("\\", ".");

                    try {

                        // 加载该类
                        Class<?> scanClass = appClassLoader.loadClass(absolutePath);
                        // 判断该类是否有@Component注解，有的话说明该类是spring bean
                        if (scanClass.isAnnotationPresent(Component.class)) {

                            // 判断类是否实现了BeanPostProcessor
                            if (BeanPostProcessor.class.isAssignableFrom(scanClass)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) scanClass.getConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessor);
                            } else {
                                //创建BeanDefinition来封装bean的属性，方便后续操作
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(scanClass);

                                /**
                                 * 判断该类是否是单例bean，如果是原型（多例）则不在此处加载，假定当前只有单例和原型两种
                                 *
                                 * 1. 如果没有@Scope注解则默认为单例bean
                                 * 2. 如果@Scope注解没有值或者值为singleton则为单例bean
                                 * 3. 如果有@Scope注解并且@Scope注解的值为scope则为原型(多例)bean
                                 */
                                if (scanClass.isAnnotationPresent(Scope.class)) {
                                    beanDefinition.setScope(scanClass.getAnnotation(Scope.class).value());
                                } else {
                                    // 单例bean
                                    beanDefinition.setScope(CommonConstant.SCOPE_SINGLETON);
                                }

                                String beanName = scanClass.getAnnotation(Component.class).value();
                                if("".equals(beanName)) {
                                    // 如果没有指定bean的名字，则默认生成一个beanName
                                    beanName = Introspector.decapitalize(scanClass.getSimpleName());
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        }

                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }
}
