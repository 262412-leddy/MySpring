package cn.ledyyer.spring.ch05;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {
    @Override // context.refresh()方法是调用
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // Spring 提供的工具类，查看类上有没有加注解
        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        try {
            if (componentScan != null) {
                for (String p : componentScan.basePackages()) {
                    System.out.println(p);
                    // cn.ledyyer.spring.ch05.component -> classpath*:cn/ledyyer/spring/ch05/component/**/*.class
                    String path = "classpath*:" + p.replace(".", "/") + "/**/*.class";
                    System.out.println(path);
                    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
                    for (Resource resource : resources) {
                        MetadataReader reader = factory.getMetadataReader(resource);
                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();

                        // 判断是否加了 @Component 注解
                        if (annotationMetadata.hasAnnotation(Component.class.getName())
                                || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {

                            // 有 @Component 注解的话，生成对应的 beanDefinition
                            AbstractBeanDefinition bd = BeanDefinitionBuilder
                                    .genericBeanDefinition(reader.getClassMetadata().getClassName())
                                    .getBeanDefinition();

                            if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                                String name = generator.generateBeanName(bd, beanFactory); // 生成bean的名字
                                beanFactory.registerBeanDefinition(name, bd);   // 找到beanFactory并且注册
                            }

                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
