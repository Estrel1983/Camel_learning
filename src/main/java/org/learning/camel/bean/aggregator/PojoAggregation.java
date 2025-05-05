package org.learning.camel.bean.aggregator;

import org.apache.camel.Exchange;

public class PojoAggregation {
    //    public Exchange stringAggregation(Exchange oldExchange, Exchange newExchange){
//        if (oldExchange == null)
//            return newExchange;
//        String oldBody = oldExchange.getIn().getBody(String.class);
//        String newBody = newExchange.getIn().getBody(String.class);
//        String mergedBody = oldBody + ' ' + newBody;
//        oldExchange.getIn().setBody(mergedBody);
//        return oldExchange;
//    }
    public String stringAggregation(String st1, String st2) {
        return st1 + ' ' + st2;
    }
}
