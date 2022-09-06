package cn.ledyyer.spring.ch04;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver()); // @Value
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders); // ${} 的解析器

        // 1. 查找哪些属性、方法加了 @Autowired 注解，这称之为 InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
//        processor.postProcessProperties(null,bean1,"bean1");
//        System.out.println(bean1);

        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class
                .getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        // 分析 Bean1 这个类中，有哪些成员变量，方法上加了 @Autowired 注解
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(metadata);

        // 2. 调用 InjectionMetadata 来进行依赖注入，注入时按类型查找
        metadata.inject(bean1, "bean1", null);  //参数1：
        System.out.println(bean1);

        // 3. 如何按类型查找值
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        Object o = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(o);


        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 =
                new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o1 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o1);

        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object o2 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(o2);
    }
}
