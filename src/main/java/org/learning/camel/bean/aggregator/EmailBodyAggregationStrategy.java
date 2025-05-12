package org.learning.camel.bean.aggregator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class EmailBodyAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            newExchange.setProperty("curNumber", 1);
            String result = "Letter #1 with text: " + newExchange.getIn().getBody() + "\n";
            newExchange.getIn().setBody(result);
            return newExchange;
        }
        String firstLetter = oldExchange.getIn().getBody(String.class);
        String secondLetter = "Letter #" + (Integer.parseInt((String) oldExchange.getProperty("curNumber")) + 1) + " with text: "
                + newExchange.getIn().getBody() + "\n";
        String result = firstLetter + secondLetter;
        oldExchange.getIn().setBody(result);
        return oldExchange;
    }
}
