package org.learning.camel.bean.transformer;

import com.sun.mail.imap.IMAPMessage;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mail.MailMessage;

import javax.mail.Message;
import java.util.Arrays;

public class SimpleTransformingProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        StringBuilder response = new StringBuilder();
        String input = exchange.getIn().getBody(String.class);
        Arrays.asList(input.split(",")).forEach(str -> response.append(str).append('\n'));
        exchange.getIn().setBody(response.toString());
    }
}
