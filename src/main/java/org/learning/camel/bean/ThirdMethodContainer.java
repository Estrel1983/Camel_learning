package org.learning.camel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.spi.Registry;

import java.util.Random;

//import java.rmi.registry.Registry;


public class ThirdMethodContainer {
    public String booleanHandler(Boolean input, Registry registry) {
        return "booleanHandler " + input + " " + (registry == null ? " null" : " notNull");
    }

    public String integerHandler(int input, Exchange exchange) {
        return "intHandler " + input + " " + (exchange == null ? " null" : exchange.getIn().getBody(Integer.class).toString());
    }
    public long getNumber(){
        Random ran = new Random();
        return ran.nextLong();
    }

}
