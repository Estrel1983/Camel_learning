package org.learning.camel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ReverseProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String input = exchange.getIn().getBody(String.class);
        String reversed = new StringBuilder(input).reverse().toString();
        exchange.getIn().setBody(reversed);
    }
}
