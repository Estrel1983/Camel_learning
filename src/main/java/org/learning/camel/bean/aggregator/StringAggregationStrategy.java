package org.learning.camel.bean.aggregator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class StringAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null)
            return newExchange;
        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);
        String mergedBody = oldBody + ' ' + newBody;
        oldExchange.getIn().setBody(mergedBody);
        return oldExchange;
    }
}
