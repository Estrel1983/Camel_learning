package org.learning.camel;

import org.apache.camel.builder.RouteBuilder;
import org.learning.camel.bean.MethodContainerBean;
import org.apache.camel.component.bean.MethodInvocation;

import java.lang.reflect.Method;

public class BeansRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("undertow:{{undertow.http}}/beans/firstMethod")
                .setHeader("CamelBeanMethodName").constant("methodFromHeader")
                .bean(MethodContainerBean.class);
        from("undertow:{{undertow.http}}/beans/altFirstMethod")
                .setHeader("CamelBeanMethodName").constant("methodFromHeader")
                .bean(MethodContainerBean.class, "callByName");
        from("undertow:{{undertow.http}}/beans/secondMethod")
                .bean(MethodContainerBean.class, "callByName");
        from("undertow:{{undertow.http}}/beans/thirdMethod")
                .to("bean:org.learning.camel.bean.MethodContainerBean");
        from("undertow:{{undertow.http}}/beans/forthMethod")
                .setBody(constant(5))
                .to("bean:org.learning.camel.bean.SecondMethodContainer");
        from("undertow:{{undertow.http}}/beans/fifthMethod")
                .setBody(constant(true))
                .to("bean:org.learning.camel.bean.SecondMethodContainer");
        from("undertow:{{undertow.http}}/beans/sixthMethod")
                .setBody(constant("Is annotated?"))
                .to("bean:org.learning.camel.bean.SecondMethodContainer");
        from("undertow:{{undertow.http}}/beans/seventhMethod")
                .setBody(constant(true))
                .to("bean:org.learning.camel.bean.ThirdMethodContainer");
        from("undertow:{{undertow.http}}/beans/eighthMethod")
                .setBody(constant(5))
                .to("bean:org.learning.camel.bean.ThirdMethodContainer");
    }
}
