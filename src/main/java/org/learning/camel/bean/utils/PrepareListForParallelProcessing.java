package org.learning.camel.bean.utils;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

public class PrepareListForParallelProcessing implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        List<String> original = exchange.getIn().getBody(List.class);
        List<String> cloned = new ArrayList<>(original);
        exchange.getIn().setBody(cloned);
    }
}
