package org.learning.camel.bean.aggregator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.nio.charset.StandardCharsets;

public class EmailBodyAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        byte[] oldBody = oldExchange.getIn().getBody(byte[].class);
        String request = new String(oldBody, StandardCharsets.UTF_8);
        if (newExchange == null) {
            oldExchange.getIn().setBody(request + "\n - There aren't new emails");
            return oldExchange;
        }
        oldExchange.getIn().setBody(request + "\nReceived email from:\n" + newExchange.getIn().getHeader("From"));
        return oldExchange;
    }
}
