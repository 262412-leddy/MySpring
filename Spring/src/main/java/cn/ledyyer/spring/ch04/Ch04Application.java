package cn.ledyyer.spring.ch04;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/*
    Bean 后处理器作用
 */
public class Ch04Application {
    public static void main(String[] args) {
        // GenericApplicationContext 是一个【干净】的容器。这个容器没有添加额外的beanFactory后处理器，bean后处理器
        GenericApplicationContext context = new GenericApplicationContext();

        // 用原始方法注册三个 bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);

        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);   // @Autowired

        context.registerBean(CommonAnnotationBeanPostProcessor.class);  // @Resource @PostConstruct @PreDestroy

        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());  // @ConfigurationProperties

        // 初始化容器
        context.refresh();  // 执行 beanFactory 后处理器，添加bean后处理器，初始化所有单例

        System.out.println(context.getBean("bean4"));
        // 关闭容器
        context.close();
    }
}
