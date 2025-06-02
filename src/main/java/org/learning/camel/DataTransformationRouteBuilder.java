package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.learning.camel.bean.MtgProcessor;
import org.learning.camel.bean.aggregator.StringAggregationStrategy;
import org.learning.camel.bean.transformer.SimpleTransformingBean;
import org.learning.camel.bean.transformer.SimpleTransformingProcessor;
import org.learning.camel.entity.MtgCard;

import java.util.Arrays;

public class DataTransformationRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        JsonDataFormat myJson = new JsonDataFormat(JsonLibrary.Jackson);
        myJson.setUseList("true");
        myJson.setUnmarshalType(MtgCard.class);
        from("undertow:{{undertow.http}}/transformation/typeConverter")
              .convertBodyTo(String.class)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Object obj = exchange.getIn().getBody();
                        exchange.getIn().setBody(obj.getClass().getName());
                }})
                .log("${body}");
        from("undertow:{{undertow.http}}/transformation/template")
                .to("direct:getCard")
                .unmarshal(myJson)
                .split(body()).aggregationStrategy(new StringAggregationStrategy())
                .to("mustache:templates/cardInfo.mustache");
        from("undertow:{{undertow.http}}/transformation/jsonToPojo")
                .unmarshal().json(JsonLibrary.Jackson, MtgCard.class)
                .process(new MtgProcessor());
        from("undertow:{{undertow.http}}/transformation/multiJsonToPojo")
                .unmarshal(myJson)
                .split(simple("${body}")).aggregationStrategy(new StringAggregationStrategy())
                    .process(new MtgProcessor());
        from("undertow:{{undertow.http}}/transformation/processor")
                .process(new SimpleTransformingProcessor());
        from("undertow:{{undertow.http}}/transformation/bean")
                .bean(new SimpleTransformingBean(), "mapStringToString");
        from("undertow:{{undertow.http}}/transformation/transform")
                .transform(new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        String input = exchange.getIn().getBody(String.class);
                        StringBuilder response = new StringBuilder();
                        Arrays.asList(input.split(",")).forEach(str -> response.append(str).append('\n'));
                        return (T) response.toString();
                    }
                });
    }
}
