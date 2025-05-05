package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jdbc.JdbcComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.dbcp2.BasicDataSource;
import org.learning.camel.bean.ArtistQueryProcessor;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

public class JdslRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        DataSource dataSource = createDataSource();
        JdbcComponent jdbcComponent = new JdbcComponent();
        jdbcComponent.setDataSource(dataSource);
        getContext().addComponent("jdbc", jdbcComponent);


        from("undertow:{{undertow.http}}/artist")
                .choice()
                    .when(header("CamelHttpMethod").isEqualTo("GET"))
                        .to("direct:getArtist")
                    .when(header("CamelHttpMethod").isEqualTo("POST"))
                        .to("direct:createArtist")
                    .otherwise()
                        .to("direct:errorHandler")
                .end();
        from("direct:ExceptionEndpoint")
                .delay(500)
                .throwException(new Exception());

        from("direct:createArtist")
                .unmarshal().json(JsonLibrary.Jackson)
                .process(new ArtistQueryProcessor())
                .to("jdbc:dataSource")
                .filter(header("CamelJdbcUpdateCount").isNotEqualTo(1))
                    .to("direct:errorHandler")
                    .stop()
                .end()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                .setBody(simple("New record successfully added"));
        from("direct:errorHandler")
                .log("HTTP method ${header.CamelHttpMethod} is not maintained")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(403))
                .setBody(simple("Forbidden: Invalid token"));
        from("direct:getArtist")
                .setBody(simple("SELECT * FROM public.artists"))
                .to("jdbc:dataSource")
                .marshal().json(JsonLibrary.Jackson)
                .log("Result: ${body}");

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
