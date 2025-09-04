package org.learning.camel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.aggregate.DefaultAggregateController;

public class DiscardProcessor implements Processor {

    private DefaultAggregateController aggregateController;

    public void setAggregateController(DefaultAggregateController aggregateController) {
        this.aggregateController = aggregateController;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String auctionId = exchange.getIn().getHeader("auctionId", String.class);
        int result = aggregateController.forceDiscardingOfGroup(auctionId);
        exchange.getIn().setBody(result == 1 ? "Auction was discarded" : "Auction remained active");
    }
}
