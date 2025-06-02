package org.learning.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jdbc.JdbcComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.dbcp2.BasicDataSource;
import org.learning.camel.bean.ArtistQueryProcessor;

import org.learning.camel.bean.aggregator.MtgCardInsertAggregationStrategy;
import org.learning.camel.entity.MtgCardInsertRequest;

import javax.sql.DataSource;

public class JdslRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        DataSource dataSource = createDataSource();
        JdbcComponent jdbcComponent = new JdbcComponent();
        jdbcComponent.setDataSource(dataSource);
        getContext().addComponent("jdbc", jdbcComponent);
        getCamelContext().getRegistry().bind("cardInsertAggregation", new MtgCardInsertAggregationStrategy());

        from("undertow:{{undertow.http}}/card")
                .choice()
                    .when(header("CamelHttpMethod").isEqualTo("GET"))
                        .to("direct:getCard")
                    .when(header("CamelHttpMethod").isEqualTo("POST"))
                        .to("direct:addCard")
                    .otherwise()
                        .to("direct:errorHandler");
        from("undertow:{{undertow.http}}/artist")
                .choice()
                    .when(header("CamelHttpMethod").isEqualTo("GET"))
                        .to("direct:getArtist")
                    .when(header("CamelHttpMethod").isEqualTo("POST"))
                        .to("direct:createArtist")
                    .otherwise()
                        .to("direct:errorHandler")
                .end();
        from("direct:addCard")
                .convertBodyTo(MtgCardInsertRequest.class)
                .setHeader("artist_name").simple("${body.artistName}")
                .setHeader("setName").simple("${body.setName}")
                .enrich()
                    .constant("direct:getArtistByName").aggregationStrategy("cardInsertAggregation")
                .aggregationStrategyMethodName("aggregateCardAndArtist")
                .enrich()
                    .constant("direct:getSetByName").aggregationStrategy("cardInsertAggregation")
                .aggregationStrategyMethodName("aggregateCardAndSet")
                .convertBodyTo(String.class)
                .to("jdbc:dataSource")
                .filter(header("CamelJdbcUpdateCount").isNotEqualTo(1))
                .to("direct:errorHandler")
                .stop()
                .end()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                .setBody(simple("New record successfully added"));
        from("direct:getCard")
                .choice()
                    .when(header("id").isNotNull())
                        .setBody().constant("SELECT * FROM public.cards WHERE id = ${headers.id}")
                    .endChoice()
                    .otherwise()
                        .setBody().constant("SELECT * FROM public.cards")
                .end()
                .to("jdbc:dataSource")
                .marshal().json(JsonLibrary.Jackson);
        from("direct:ExceptionEndpoint")
                .delay(500)
                .throwException(new Exception());
        from("direct:getSetByName")
                .setBody(constant("SELECT * FROM public.setname WHERE set_name = :?setName"))
                .to("jdbc:dataSource?useHeadersAsParameters=true&outputType=SelectOne&outputClass=org.learning.camel.entity.Set");
        from("direct:getArtistByName")
                .setBody(constant("SELECT * FROM public.artists WHERE artist_name = :?artist_name"))
                .to("jdbc:dataSource?useHeadersAsParameters=true&outputType=SelectOne&outputClass=org.learning.camel.entity.Artist");
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
