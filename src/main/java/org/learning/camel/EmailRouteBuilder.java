package org.learning.camel;

import org.apache.camel.builder.RouteBuilder;
import org.learning.camel.bean.MessageFilterBean;

public class EmailRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("imaps://{{mail.host}}:{{mail.port}}?username={{mail.username}}&password={{mail.password}}&delete=false&unseen=true&folderName=Camel&delay=600000")
                .log("Successful receiving")
                .filter().method(MessageFilterBean.class, "emailFilter")
                    .log("${body}")
                .log("All letters are received")
                .setBody(constant("New Body"))
                .to("direct:emailSender");
        from("direct:emailSender")
                .setHeader("Subject", constant("Body Transformation"))
                .setHeader("To", constant("shmyrovyury@gmail.com"))
                .setHeader("From", constant("{{mail.username}}"))
                .to("smtps:{{mail.smtps.host}}:{{mail.smtps.port}}?username={{mail.username}}&password={{mail.password}}");
    }
}
