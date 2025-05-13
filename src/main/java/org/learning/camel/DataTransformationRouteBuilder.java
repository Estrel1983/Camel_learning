package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.learning.camel.bean.transformer.SimpleTransformingBean;
import org.learning.camel.bean.transformer.SimpleTransformingProcessor;

import java.util.Arrays;

public class DataTransformationRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
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
