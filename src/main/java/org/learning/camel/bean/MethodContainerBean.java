package org.learning.camel.bean;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Processor;

public class MethodContainerBean implements Processor {
    public String methodFromHeader(){
        return "It's from header";
    }
    public String callByName(){
        return "Called by name";
    }

    @Handler
    public String handler(){
        return "Handler Annotation";
    }
    public String handler(@Body String body){
        return "Handler Annotation";
    }
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody("It's processor");
    }
}
