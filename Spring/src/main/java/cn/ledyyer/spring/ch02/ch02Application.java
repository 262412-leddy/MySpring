package cn.ledyyer.spring.ch02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

/*
    常见 ApplicationContext 接口实现
 */
public class ch02Application {
    private static final Logger log = LoggerFactory.getLogger(ch02Application.class);

    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testFileSystemXmlApplicationContext();
//        testAnnotationConfigApplicationContext();
//        testAnnotationConfigServletWebServerApplicationContext();

        /*
        //下面的代码是 基于xml创建spring容器的内部逻辑
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("读取之前：");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("读取之后：");
        // 读取 xml Bean定义信息
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);  // 告诉读取器将读取完的Bean丢到拿个容器
        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));   // 读取配置
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
         */

        /*

         */

    }

    // 较为经典的容器，基于 classpath 下 xml 格式的配置文件来创建
    // 时至今日呢，基于Xml的容器配置是比较少的，现在都是使用配置类，或是自动配置类进行配置
    private static void testClassPathXmlApplicationContext(){
        log.debug("ClassPathXmlApplicationContext 测试：");
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("b01.xml");

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // 👇 基于磁盘路径下 xml 格式的配置文件来创建
    private static void testFileSystemXmlApplicationContext(){
        log.debug("FilePathXmlApplicationContext 测试：");
        FileSystemXmlApplicationContext context =
                new FileSystemXmlApplicationContext("/Spring/src/main/resources/b01.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // 👇 较为经典的容器，基于 java 配置类来创建
    private static void testAnnotationConfigApplicationContext(){
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // 👇 较为经典的容器，基于 Java 配置类创建容器，用于 web 环境
    private static void testAnnotationConfigServletWebServerApplicationContext(){
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

    @Configuration
    static class WebConfig{
        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        // Spring的Web层面的技术核心在于 dispatcherServlet，将来客户端发送的请求的入口点就是 dispatcherServlet 前控制器
        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        @Bean
        // 将 dispatcherServlet 注册到 内嵌的 Tomcat 服务器中
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet){
            return new DispatcherServletRegistrationBean(dispatcherServlet,"/");
        }

        @Bean("/hello")
        public Controller controller1(){
            return (request, response) ->{
                response.getWriter().print("Hello World!");
                return null;
            };
        }
    }

    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(Bean1 bean1){
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }
    }
    static class Bean1{}

    static class Bean2{
        private Bean1 bean1;

        public Bean1 getBean1() {
            return bean1;
        }

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }

    }

    @Service
    static class service{

    }
}
