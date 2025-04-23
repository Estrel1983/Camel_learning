package org.learning.camel;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file://c:/Private/Repos/Camel_learning/data/inbox?noop=true")
                .to("file:data/outbox");
    }
}
