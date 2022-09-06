package cn.ledyyer.spring.ch02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

public class TestBeanFactory {
    public static void main(String[] args) {
        // DefaultListableBeanFactory 是 BeanFactory 最重要的一个实现
         DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // BeanFactory 根据所给的 bean 的定义创建 bean
        // bean 的定义（class，scope，初始化，销毁）
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).getBeanDefinition();
        beanFactory.registerBeanDefinition("config",beanDefinition);

        // 1. 打印 beanFactory 中的 bean，只有一个刚刚加入的 config 类
        // 明明在 Config 类上加了 @Configuration，在类中的方法加了@Bean，为什么没有呢？
        // 原因是：BeanFactory 类的功能有限，不能做这些操作，很多高大上的操作都是后续子类扩展上去的
        System.out.println("加入后置处理器之前的beanFactory中有：");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        // 注解配置相关的注解类。作用：给 beanFactory 中添加一些常用的后置处理器
        // 后置处理器的作用就是对 beanFactory 功能的扩展
        /* 如下，就是 Spring 内置的后置处理器：
            org.springframework.context.annotation.internalConfigurationAnnotationProcessor
                - 处理@Configuration注解，以及配置类中的@Bean注解

            org.springframework.context.annotation.internalAutowiredAnnotationProcessor
                - 解析 @Autowired、@Value

            org.springframework.context.annotation.internalCommonAnnotationProcessor
                - 解析 @Resource 注解

            org.springframework.context.event.internalEventListenerProcessor
                - 接卸
            org.springframework.context.event.internalEventListenerFactory
                -
         */
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        System.out.println("===================================================");
        System.out.println("加入后置处理器之后的beanFactory中有：");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        // 在给 beanFactory 添加了后置处理器后，beanFactory 并不会自动调用，所以此时也不能解析@Configuration和@Bean注解
        // 需要手动调用，这个方法是根据后置处理器的类型拿到后置处理器们，然后通过foreach用每一个后置处理器去加工beanFactory
        // BeanFactory 后置处理器主要功能是补充了一些 bean 定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        System.out.println("===================================================");
        System.out.println("加入并执行后置处理器之后的beanFactory中有：");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        // 使用 bean1
        // 当我们使用bean1 的 getBean2 方法时，返回值是 null，说明 bean2 没有被注入到 bean1 中。也就说 @Autowired 没起作用
        // 此时需要使用 bean 的后置处理器
//        System.out.println("===================================================");
//        Bean1 bean1 = (Bean1) beanFactory.getBean(Bean1.class);
//        System.out.println(bean1);
//        System.out.println(bean1.getBean2());

        // 调用 bean 后置处理器
        // bean 后置处理器的作用：针对 bean 的声明周期的各个阶段提供扩展，例如 @Autowired @Resource
        // 默认延迟创建bean，在使用bean的时候才创建
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);
        System.out.println("===================================================");
        Bean1 bean1 = beanFactory.getBean(Bean1.class);
        System.out.println(bean1.getBean2());

        /*
            beanFactory 不会做的事情：
                1. 不会主动调用 BeanFactory
                2. 不会主动添加 Bean 后处理器
                3. 不会主动初始化单例
                4. 不会解析beanFactory 还不会解析 ${} #{}
         */
    }

    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1() {return new Bean1();}

        @Bean
        public Bean2 bean2() {return new Bean2();}
    }

    static class Bean1{
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1(){
            log.debug("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2(){
            return bean2;
        }
    }

    static class Bean2{
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2(){
            log.debug("构造 Bean2()");
        }
    }
}
