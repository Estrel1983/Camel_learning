package org.learning.camel.greeting;

public class GreetingBean {
    private Greeter greeter;
    public void setGreeter(Greeter greeter){
        this.greeter = greeter;
    }
    public void execute(){
        System.out.println(greeter.sayHello());
    }
}
