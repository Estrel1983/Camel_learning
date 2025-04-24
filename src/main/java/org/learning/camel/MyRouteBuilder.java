package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        PropertiesComponent prop = new PropertiesComponent("classpath:additional.properties");
//        getContext().setPropertiesComponent(prop);
        from("jetty:http://localhost:8080/test-endpoint")
                .log("Body before processor ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String input = exchange.getIn().getBody(String.class);
                        String reversed = new StringBuilder(input).reverse().toString();
                        exchange.getIn().setBody(reversed);
                    }
                })
                .log("Body after processor ${body}")
                .to("file:data/outbox/1");
        from("undertow:http://localhost:{{undertow.port}}/test-endpoint")
                .log("Received POST body ${body}")
                .toD("file:data/outbox/${header.Folder}");
        from("undertow:http://localhost:8081/loop-endpoint")
                .log("Received POST body ${body}")
                .to("direct:headerSetter");
        from("direct:headerSetter")
                .log("Header setting")
                .setHeader("direction", simple("undertow:http://localhost:8081/loop-endpoint"))
                .to("direct:loopRout");
        from("direct:loopRout")
                .log("Body in additional Root ${body}")
                .toD("${header.direction}");
    }
}
