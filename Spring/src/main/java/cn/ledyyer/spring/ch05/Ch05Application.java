package cn.ledyyer.spring.ch05;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class Ch05Application {
    private static final Logger log = LoggerFactory.getLogger(Ch05Application.class);

    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
//        context.registerBean(ConfigurationClassPostProcessor.class);    // @ComponentScan @Bean @Import @ImportResource
//        context.registerBean(MapperScannerConfigurer.class, bd -> {
//            bd.getPropertyValues().add("basePackage", "cn.ledyyer.spring.ch05.mapper");
//        }); // @MapperScanner

        context.registerBean(ComponentScanPostProcessor.class);

        // 初始化容器
        context.refresh();

        for (String name : context.getBeanDefinitionNames()){
            System.out.println(name);
        }

        // 销毁容器
        context.close();

    }
}
