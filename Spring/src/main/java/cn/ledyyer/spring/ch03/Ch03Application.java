package cn.ledyyer.spring.ch03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Ch03Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Ch03Application.class, args);
        LifeCycleBean liftCycleBean = (LifeCycleBean) context.getBean("lifeCycleBean");
        MyBeanPostProcessor myBeanPostProcessor = (MyBeanPostProcessor) context.getBean("myBeanPostProcessor");
        // 容器的关闭方法
        context.close();
    }
}
