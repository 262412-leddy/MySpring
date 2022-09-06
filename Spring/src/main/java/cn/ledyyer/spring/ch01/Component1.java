package cn.ledyyer.spring.ch01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Component1 {
    @Autowired
    private ApplicationEventPublisher context;

    private static void hello(){
        System.out.println("Hello World 1");
    }

    public void register(){
        System.out.println("用户注册");
        context.publishEvent(new UserRegisteredEvent(this));
    }
}
