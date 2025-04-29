package org.learning.camel.bean;

import org.apache.camel.Exchange;

import java.util.HashSet;

public class MessageFilterBean {
    public static final HashSet<String> stringContainer = new HashSet<>();
    public boolean allowedOnlyUnique(Exchange exchange){
        String curString = exchange.getIn().getBody(String.class);
        return stringContainer.add(curString);
    }
}
