package org.learning.camel;

import org.apache.camel.builder.RouteBuilder;
import org.learning.camel.bean.MethodContainerBean;
import org.apache.camel.component.bean.MethodInvocation;
import org.learning.camel.bean.ThirdMethodContainer;
import org.learning.camel.bean.TstMethodContainer;

import java.lang.reflect.Method;

public class BeansRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        getCamelContext().getRegistry().bind("guid", ThirdMethodContainer.class);
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
        from("undertow:{{undertow.http}}/beans/annotatedMethodWithHeader")
                .setHeader("myString", constant("String from header"))
                .to("bean:org.learning.camel.bean.TstMethodContainer");
        from("undertow:{{undertow.http}}/beans/annotatedMethodWithoutHeader")
                .setHeader("myString", constant("String from header"))
                .to("bean:org.learning.camel.bean.TstMethodContainer?method=callingAnnotatedMethod");
        from("undertow:{{undertow.http}}/beans/annotatedMethodAutoSetup")
                .setHeader("myString", constant("String from header"))
                .setHeader("tmpHeader", constant("String from tmpHeader"))
                .bean(TstMethodContainer.class, "annotatedMethod(*, ${header.tmpHeader})");
        from("undertow:{{undertow.http}}/beans/annotatedWithBean")
                .to("bean:org.learning.camel.bean.TstMethodContainer?method=addNumberFromBean");
    }
}
