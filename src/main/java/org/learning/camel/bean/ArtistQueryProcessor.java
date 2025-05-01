package org.learning.camel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.LinkedHashMap;

public class ArtistQueryProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        LinkedHashMap<String, String> listOfArtists = exchange.getIn().getBody(LinkedHashMap.class);
        String sql = "INSERT INTO public.artists (artist_name) VALUES ('" + listOfArtists.get("artist_name")+"');";
        exchange.getIn().setBody(sql);
    }
}
