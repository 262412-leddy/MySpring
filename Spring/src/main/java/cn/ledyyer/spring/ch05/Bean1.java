package cn.ledyyer.spring.ch05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Bean1 {
    private static final Logger log = LoggerFactory.getLogger(Bean1.class);

    public Bean1(){
        log.debug("我被 Spring 管理啦！");
    }
}
