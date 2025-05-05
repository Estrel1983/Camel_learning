package org.learning.camel.bean.utils;


import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;

public class BodySetter {
    public void setStringAsList(Exchange ex){
        ex.getIn().setBody(new ArrayList<>(List.of(ex.getIn().getHeader("forBody"))));
    }
    public void setStringWithThreadAsList(Exchange ex){
        List<String> list = ex.getIn().getBody(List.class);
        String threadName = Thread.currentThread().getName();
        for(int i=0; i<5; i++)
            list.add(threadName + " " +i);
    }
}
