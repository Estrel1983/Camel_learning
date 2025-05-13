package org.learning.camel.bean.transformer;

import java.util.Arrays;

public class SimpleTransformingBean {
    public String mapStringToString(String input){
        StringBuilder response = new StringBuilder();
        Arrays.asList(input.split(",")).forEach(str -> response.append(str).append('\n'));
        return response.toString();
    }
}
