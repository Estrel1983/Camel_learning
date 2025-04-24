package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        from("file://c:/Private/Repos/Camel_learning/data/inbox?noop=true")
//                .to("file:data/outbox");
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
        from("undertow:http://localhost:8081/test-endpoint")
                .log("Received POST body ${body}")
                .toD("file:data/outbox/${header.Folder}");
    }
}
