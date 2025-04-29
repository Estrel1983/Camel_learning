package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.learning.camel.bean.MessageFilterBean;

public class EipRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("undertow:{{undertow.http}}/filtersInRout")
                .filter(simple("${header.filtered} == 'true'"))
                        .log("Passed the filter.")
                        .stop()
                .end()
                .log("Did not pass the filter");
        from("undertow:{{undertow.http}}/complexFiltersInRout")
                .filter().method(MessageFilterBean.class, "allowedOnlyUnique")
                .statusPropertyName("isFiltered")
                        .log("Passed the filter - ${body}")
                        .log("Now with property ${exchangeProperty.isFiltered}")
                        .stop()
                        .end()
                .log("This string already exists - ${body}")
                .log("Now with property ${exchangeProperty.isFiltered}");
        from("undertow:{{undertow.http}}/cb_router")
                .choice()
                .precondition(true)
                .when(simple("{{?foo}}")).log("This route for foo").stop().endChoice()
                .when(simple("{{?bar}}")).log("This route for bar").stop().endChoice()
                .otherwise().log("Otherwise rout").stop();
//        from("undertow:{{undertow.http}}/test-endpoint")
//                .choice().description("We determine whether the file needs to be copied based on the \"isCopied\" header")
//                .when(header("isCopied").isEqualTo(true))
//                .log("Will be saved")
//                        .loadBalance().roundRobin().toD("file:data/outbox/${header.Folder}").end().endChoice()
//                .toD("file:data/outbox/${header.Folder}")
//                .otherwise()
//                .log("Will not be saved")
//                .stop()
        from("undertow:{{undertow.http}}/routAfterChoice")
                .choice()
                .when(header("isCopied").isEqualTo(true))
                    .log("Will be saved")
                .when(header("isCopied").isEqualTo(false))
                    .log("Will not be saved")
                .otherwise()
                    .log("Everything is broken")
                    .stop()
                .end()
                .log("Routing after choice");
    }
}
