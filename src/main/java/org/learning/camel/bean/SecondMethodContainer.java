package org.learning.camel.bean;

import org.apache.camel.Body;
import org.apache.camel.Exchange;

public class SecondMethodContainer {
    public String inBody(int input){
        return input + " is integer";
    }
    public String inBody(boolean input){
        return input + " is boolean";
    }
    public String inBody(int input, Exchange exchange){
        return input + " is integer with exchange";
    }
    public String annotatedBody(@Body String input){
        return input + " from annotated method";
    }
    public String normalBody(String input){
        return input + " from method without annotation";
    }
}
