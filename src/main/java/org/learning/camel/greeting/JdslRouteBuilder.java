package org.learning.camel.greeting;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jdbc.JdbcComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class JdslRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        DataSource dataSource = createDataSource();
        JdbcComponent jdbcComponent = new JdbcComponent();
        jdbcComponent.setDataSource(dataSource);
        getContext().addComponent("jdbc", jdbcComponent);

        from("undertow:{{undertow.http}}/getArtist")
                .setBody(simple("SELECT * FROM public.artists"))
                .to("jdbc:dataSource")
                .marshal().json(JsonLibrary.Jackson)
                .log("Result: ${body}");
    }
    private DataSource createDataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/carddb");
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        return ds;
    }
}
