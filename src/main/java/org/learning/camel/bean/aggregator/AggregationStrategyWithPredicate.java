package org.learning.camel.bean.aggregator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class AggregationStrategyWithPredicate implements Predicate, AggregationStrategy{
    @Override
    public boolean matches(Exchange exchange) {
        return "true".equals(exchange.getIn().getHeader("isDone", String.class));
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if(oldExchange == null)
            return newExchange;
        String firstString = oldExchange.getIn().getBody(String.class);
        String secondString = newExchange.getIn().getBody(String.class);
        oldExchange.getIn().setBody(firstString + secondString);
        if (newExchange.getIn().getHeader("isDone")!=null)
            oldExchange.getIn().setHeader("isDone", newExchange.getIn().getHeader("isDone", String.class));
        return oldExchange;
    }
}
