package org.learning.camel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.learning.camel.entity.MtgCard;

public class MtgProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        MtgCard mtgCard = exchange.getIn().getBody(MtgCard.class);
        exchange.getIn().setBody(mtgCard.getCardName().getBytes());
    }
}
