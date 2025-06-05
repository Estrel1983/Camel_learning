package org.learning.camel;

import org.apache.camel.main.Main;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.spring.SpringCamelContext;
import org.learning.camel.bean.converter.CardConverter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class that boot the Camel application
 */
public final class MyApplication {

    private MyApplication() {
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main(MyApplication.class);
        main.setPropertyPlaceholderLocations("classpath:additional.properties,classpath:mail.properties");
        main.run(args);
    }
//    public static void main(String[] args) throws Exception {
//        ClassPathXmlApplicationContext context =
//                new ClassPathXmlApplicationContext("CamelContext.xml");
//        SpringCamelContext scc = context.getBean(SpringCamelContext.class);
//        TypeConverterRegistry registry = scc.getTypeConverterRegistry();
//        registry.addTypeConverters(new CardConverter());
//        context.registerShutdownHook();
//        System.out.println("Application started. Press Ctrl+C");
//    }
//    public static void main(String[] args) throws Exception {
//        CamelContext context = new DefaultCamelContext();
//        context.addRoutes(new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("file://c:/Private/Repos/Camel_learning/data/inbox?noop=true")
//                        .to("file:data/outbox");
//            }
//        });
//        context.start();
//        Thread.sleep(5000);
//        context.stop();
//    }

}
