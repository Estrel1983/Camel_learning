package org.learning.camel.bean;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.language.bean.Bean;

public class TstMethodContainer {
    public String annotatedMethod(String bodyString, @Header("myString") String additionalString){
        return bodyString + " and " + additionalString;
    }
    public String callingAnnotatedMethod(String bodyString){
        return annotatedMethod(bodyString, "Additional string");
    }
    public String addNumberFromBean(@Body String bodyString, @Bean(ref="guid", method = "getNumber") int number){
        return bodyString + " " + number;
    }
}
