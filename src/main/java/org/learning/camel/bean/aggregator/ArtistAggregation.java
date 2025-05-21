package org.learning.camel.bean.aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class ArtistAggregation implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        LinkedHashMap<String, String> oldBodyJson = oldExchange.getIn().getBody(LinkedHashMap.class);
        String artistName = oldBodyJson.get("artistName");
        if (newExchange == null) {
            oldBodyJson.put("isExists", "false");
            oldExchange.getIn().setBody(oldBodyJson);
            return oldExchange;
        }
        byte[] bodyBytes = newExchange.getIn().getBody(byte[].class);
        String artistJson = new String(bodyBytes, StandardCharsets.UTF_8);
        boolean exists = artistJson.contains(artistName);
        oldBodyJson.put("isExists", Boolean.toString(exists));
        oldExchange.getIn().setBody(oldBodyJson);
        return oldExchange;
    }
}
