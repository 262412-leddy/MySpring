package cn.ledyyer.spring.ch01;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Component2 {
    private static void hello(){
        System.out.println("Hello World 2");
    }

    @EventListener
    public void aaa(UserRegisteredEvent userRegisteredEvent){
        System.out.println(userRegisteredEvent);
        System.out.println("发送短信");
    }
}
