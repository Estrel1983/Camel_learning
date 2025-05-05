package org.learning.camel.bean.aggregator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;

public class ListAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null)
            return newExchange;
        List<String> oldList = oldExchange.getIn().getBody(List.class);
        List<String> newList = newExchange.getIn().getBody(List.class);
        List<String> combinedList = new ArrayList<>(oldList);
        combinedList.addAll(newList);
        oldExchange.getIn().setBody(combinedList);
        return oldExchange;
    }
}
