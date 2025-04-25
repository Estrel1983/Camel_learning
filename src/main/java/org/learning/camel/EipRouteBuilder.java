package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class EipRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("undertow:{{undertow.http}}/cb_router")
                .choice()
                .precondition(true)
                .when(simple("{{?foo}}")).log("This route for foo").stop().endChoice()
                .when(simple("{{?bar}}")).log("This route for bar").stop().endChoice()
                .otherwise().log("Otherwise rout").stop();
        from("undertow:{{undertow.http}}/test-endpoint")
                .choice().description("We determine whether the file needs to be copied based on the \"isCopied\" header")
                .when(header("isCopied").isEqualTo(true))
                .log("Will be saved")
//                        .loadBalance().roundRobin().toD("file:data/outbox/${header.Folder}").end().endChoice()
                .toD("file:data/outbox/${header.Folder}")
                .otherwise()
                .log("Will not be saved")
                .stop();
    }
}
