package org.learning.camel.bean.expressions;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.processor.aggregate.DefaultAggregateController;
import org.apache.camel.processor.aggregate.MemoryAggregationRepository;

public class SimpleCorrelationExpression implements Expression {
    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        System.out.println("Evaluating correlation key, requested type: " + type.getName());
        Object key = exchange.getIn().getHeader("aggregate", Integer.class);
        if (key.equals(1)) {
            return exchange.getContext().getTypeConverter().convertTo(type, 1);
        }
        return exchange.getContext().getTypeConverter().convertTo(type, 2);
    }

    @Override
    public void init(CamelContext context) {
        Expression.super.init(context);
    }
}
