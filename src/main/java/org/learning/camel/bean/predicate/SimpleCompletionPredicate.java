package org.learning.camel.bean.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class SimpleCompletionPredicate implements Predicate {
    @Override
    public boolean matches(Exchange exchange) {
        if (exchange.getIn().getHeader("isDone") == null)
            return false;
        return "done".equals(exchange.getIn().getHeader("isDone") );
    }
}
