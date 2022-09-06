package cn.ledyyer.spring.ch03;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplate {
    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addBeanPostProcessor(obj -> System.out.println("解析 @Autowired"));
        beanFactory.addBeanPostProcessor(obj -> System.out.println("解析 @Resource"));
        beanFactory.getBean();
    }

    // 模板方法
    static class MyBeanFactory{
        public Object getBean(){
            Object bean = new Object();
            System.out.println("构造 " + bean);
            System.out.println("依赖注入 " + bean);
            for(BeanPostProcessor processor: processors){
                processor.inject(bean);
            }
            System.out.println("初始化 " + bean);
            return bean;
        }

        private List<BeanPostProcessor> processors = new ArrayList<>();

        public void addBeanPostProcessor(BeanPostProcessor processor){
            processors.add(processor);
        }
    }

    interface BeanPostProcessor{
        void inject(Object obj); // 对依赖注入阶段的扩展
    }
}
