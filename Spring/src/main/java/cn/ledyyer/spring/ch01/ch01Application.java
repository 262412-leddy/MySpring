package cn.ledyyer.spring.ch01;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/*
    SpringFactory 与 ApplicationContext 的区别
 */
@SpringBootApplication
public class ch01Application {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        // run 方法具有返回值，返回的就是 Spring 容器，可配置的ApplicationContext
        // ConfigurableApplicationContext 是 ApplicationContext 的子接口
        ConfigurableApplicationContext context = SpringApplication.run(ch01Application.class, args);
        /*
            1. 到底什么是 BeanFactory
                - 它是 ApplicationContext 的父亲接口
                - 它才是 Spring 的核心容器，主要的 ApplicationContext 实现都【组合】了它的功能
         */
        //context.getBean();

        /*
            2. BeanFactory 能干啥？
                - 表面上只有 getBean
                - 实际上控制反转、基本的依赖注入、直至 Bean 的声明周期的各种功能，都由它的实现类提供
         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(e->e.getKey().startsWith("component"))
                .forEach(e->{
                    System.out.println(e.getKey() + " = " + e.getValue());
                }
        );

        /*
            3. ApplicationContext 比 BeanFactory 多点啥？
                - 功能1：国际化，可以翻译
                - 功能2：获取文件资源
                - 功能3：获取环境变量
                - 功能4：
        */

//        System.out.println(context.getMessage("hi", null, Locale.CHINESE));
//        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));
//        System.out.println(context.getMessage("hi", null, Locale.JAPANESE));

        // 找类路径下的资源 classpath
        // 找jar包类路径下的资源：classpath*
        Resource[] resources = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources){
            System.out.println(resource);
        }

        // 获取环境变量，可以来自于系统变量，可以来自于主配置文件
        System.out.println(context.getEnvironment().getProperty("java_home"));

        // 一种解耦方式
        context.publishEvent(new UserRegisteredEvent(context));

        context.getBean(Component1.class).register();
    }

}
