package org.learning.camel;

import org.apache.camel.builder.RouteBuilder;
import org.learning.camel.bean.MessageFilterBean;
import org.learning.camel.bean.transformer.SimpleTransformingProcessor;

public class EmailRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        from("direct:emailSender")
//                .log("sender")
//                .setHeader("Subject", constant("Body Transformation"))
//                .setHeader("To", constant("shmyrovyury@gmail.com"))
//                .setHeader("From", constant("{{mail.username}}"))
//                .to("smtps:{{mail.smtps.host}}:{{mail.smtps.port}}?username={{mail.username}}&password={{mail.password}}");
//        from("imaps://{{mail.host}}:{{mail.port}}?username={{mail.username}}&password={{mail.password}}&delete=false&unseen=true&folderName=Camel&delay=600000")
//                .log("Successful receiving")
//                .filter().method(MessageFilterBean.class, "emailFilter")
//                .log("${body}")
//                .log("All letters are received")
//                .setBody(constant("New Body"))
//                .to("direct:emailSender");
    }
}
