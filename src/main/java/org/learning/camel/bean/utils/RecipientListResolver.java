package org.learning.camel.bean.utils;

import org.apache.camel.Exchange;
import org.apache.camel.RecipientList;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RecipientListResolver {
    @RecipientList(aggregationStrategy = "stringAggregationStrategy", ignoreInvalidEndpoints = true)
    public ArrayList<String> getRecipientList(Exchange exchange) {
        ArrayList<String> result = new ArrayList<>();

        if (exchange.getIn().getHeader("firstRout", boolean.class))
            result.add("direct:firstDestination");
        if (exchange.getIn().getHeader("secondRout", boolean.class))
            result.add("direct:secondDestination");
        if (exchange.getIn().getHeader("thirdRout", boolean.class))
//            result.add("abracadabra");
            result.add("direct:thirdDestination");
        return result;
    }
//    @RecipientList(aggregationStrategy = "stringAggregationStrategy", delimiter = "0")
//    public String getRecipientList(Exchange exchange) {
//        return "direct:firstDestination" + '0' +
//                "direct:secondDestination" + '0' +
//                "direct:thirdDestination";
//    }
}