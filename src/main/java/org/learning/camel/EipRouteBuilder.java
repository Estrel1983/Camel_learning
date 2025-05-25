package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.ExpressionAdapter;
import org.learning.camel.bean.MessageFilterBean;
import org.learning.camel.bean.aggregator.*;
import org.learning.camel.bean.utils.RecipientListResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class EipRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        ExecutorService executor = getCamelContext().getExecutorServiceManager().newFixedThreadPool(this, "myPool", 2);
        getCamelContext().getRegistry().bind("pojoAggregation", new PojoAggregation());
        getCamelContext().getRegistry().bind("stringAggregationStrategy", new StringAggregationStrategy());
        JsonDataFormat myJson = new JsonDataFormat(JsonLibrary.Jackson);
        ArrayList<String> recList = new ArrayList<>(List.of("direct:firstDestination", "direct:secondDestination", "direct:thirdDestination"));

        from("undertow:{{undertow.http}}/pollEnricher")
                .pollEnrich("imaps://{{mail.host}}:{{mail.port}}?username={{mail.username}}&password={{mail.password}}&delete=false&unseen=true&folderName=Camel",
                        new EmailBodyAggregationStrategy());
        from("undertow:{{undertow.http}}/isArtistExistsEnricher")
                .unmarshal(myJson)
                        .enrich().constant("direct:getArtist").aggregationStrategy(new ArtistAggregation())
                        .marshal().json(JsonLibrary.Jackson);
        from("undertow:{{undertow.http}}/wireTap")
                .setBody(constant("before"))
                .wireTap("direct:wireTap")
                .to("direct:onlyLog");
        from("direct:wireTap")
                .log("WireTap body - ${body}");
        from("direct:onlyLog")
                .log("Body - ${body}");
        from("undertow:{{undertow.http}}/recipientListAnnotatedWithCash")
                .recipientList(new ExpressionAdapter() {
                    @Override
                    public Object evaluate(Exchange exchange) {
                        List<String> recipients = new ArrayList<>();
                        for(int i=0; i<10; i++){
                            recipients.add("direct:cash"+i);
                        }
                        return recipients;
                    }
                }).cacheSize(-1).parallelProcessing();
        from("direct:cash0").log("0");
        from("direct:cash1").log("1");
        from("direct:cash2").log("2");
        from("direct:cash3").log("3");
        from("direct:cash4").log("4");
        from("direct:cash5").log("5");
        from("direct:cash6").log("6");
        from("direct:cash7").log("7");
        from("direct:cash8").log("8");
        from("direct:cash9").log("9");
        from("undertow:{{undertow.http}}/recipientListAnnotated")
                .bean(new RecipientListResolver())
                .log("Result : ${body}");
        from("undertow:{{undertow.http}}/recipientList")
                .recipientList().method(RecipientListResolver.class,"getRecipientList")
                .aggregationStrategy(new StringAggregationStrategy())
                .log("Result : ${body}");
        from("undertow:{{undertow.http}}/pojoAggregation")
                .multicast()
                .streaming()
                .aggregationStrategy("pojoAggregation")
                .aggregationStrategyMethodAllowNull()
                .aggregationStrategyMethodName("stringAggregation")
                .to("direct:firstDestination", "direct:secondDestination", "direct:thirdDestination")
                .end()
                .log("Result : ${body}");
        from("undertow:{{undertow.http}}/multicastSharedState")
                .setBody(constant(new ArrayList<>(List.of("Starting message"))))
                .multicast()
                    .onPrepare(e -> {
                        List<String> original = e.getIn().getBody(List.class);
                        List<String> cloned = new ArrayList<>(original);
                        e.getIn().setBody(cloned);
                    })
                    .parallelProcessing(true)
                    .aggregationStrategy(new ListAggregationStrategy())
                    .to("direct:listChanger", "direct:listChanger", "direct:listChanger")
                .end()
                .log("Final Body is: ${body}")
                .marshal().json(JsonLibrary.Jackson);

        from("undertow:{{undertow.http}}/multicastOnPrepare")
                .log("Password is - ${header.MyPassword}")
                .multicast().onPrepare(exchange -> {
                    exchange.getIn().removeHeader("MyPassword");
                })
                        .to("direct:firstDestination", "direct:unsafeDestination", "direct:thirdDestination")
                .end();
        from("undertow:{{undertow.http}}/multicastWithException")
                .multicast()
                        .stopOnException()
                        .aggregationStrategy(new StringAggregationStrategy())
                        .to("direct:firstDestination", "direct:brokenDestination", "direct:thirdDestination")
                .end()
                .log("Result : ${body}");
        from("direct:propertyChanger")
                .process(e->{e.setProperty("myProperty", "property changed");});
        from("direct:bodySetter")
                .delay(100)
                .process(e->{
                    String prop = (String) e.getProperty("myProperty");
                    if(prop != null && prop.equals("property changed"))
                        e.getIn().setBody("All works");
                    else
                        e.getIn().setBody("Nothing works");
                })
                .log("We are here");
        from("direct:listChanger")
                .process(e -> {
                    List<String> list = e.getIn().getBody(List.class);
                    String threadName = Thread.currentThread().getName();
                    for(int i=0; i<5; i++)
                        list.add(threadName + " " +i);
                });

        from("direct:brokenDestination")
                .throwException(new Exception())
                .setBody(constant("Broken Body"))
                .log("This is broken rout");
        from("undertow:{{undertow.http}}/multicast")
                .multicast()
                    .streaming()
                    .parallelProcessing(true)
                    .executorService(executor)
                    .aggregationStrategy(new StringAggregationStrategy())
                    .to("direct:firstDestination", "direct:secondDestination", "direct:thirdDestination")
                .end()
                .log("Result : ${body}");
        from("undertow:{{undertow.http}}/multicastWithDelay")
                .multicast()
                .streaming()
                .parallelProcessing(true)
                .timeout(300)
                .aggregationStrategy(new StringAggregationStrategy())
                .to("direct:firstDestination", "direct:delayDestination", "direct:thirdDestination")
                .end()
                .log("Result : ${body}");
        from("direct:unsafeDestination")
                .log("Password is - ${header.MyPassword}");
        from("direct:firstDestination")
                .setBody(simple("First Body"))
                .log("First thread: ${threadName}")
                .log("This is first route");
        from("direct:secondDestination")
                .setBody(simple("Second Body"))
                .log("Second thread: ${threadName}")
                .log("This is second route");
        from("direct:thirdDestination")
                .setBody(simple("Third Body"))
                .log("Third thread: ${threadName}")
                .log("This is third route");
        from("direct:delayDestination")
                .setBody(simple("Delay Body"))
                .delay(600)
                .log("This is route with delay");

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
