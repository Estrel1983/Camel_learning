package org.learning.camel.bean;

import org.apache.camel.Exchange;

public class RoutingSlip {
    public String getSlip(Exchange e){
        String language = e.getIn().getHeader("language", String.class);
        String source = e.getIn().getHeader("source", String.class);
        StringBuilder slip = new StringBuilder();
        if ("Amazon".equals(source))
            slip.append("direct:enrichAmazonMessage,");
        slip.append("direct:").append(language);
        return slip.toString();
    }
}
