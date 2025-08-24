package org.learning.camel.bean.aggregator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuctionAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String nowStr = LocalDateTime.now().format(formatter);
            newExchange.getIn().setHeader("startingTime", nowStr);
            return newExchange;
        }
        Integer oldBid = oldExchange.getIn().getHeader("bid", Integer.class);
        Integer newBid = newExchange.getIn().getHeader("bid", Integer.class);
        if (newBid != null && oldBid != null && newBid > oldBid) {
            oldExchange.getIn().setHeader("owner", newExchange.getIn().getHeader("owner"));
            oldExchange.getIn().setHeader("bid", newBid);
        }
        return oldExchange;
    }
}
