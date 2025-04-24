package org.learning.camel.greeting;

public class EnglishGreeting implements Greeter {
    @Override
    public String sayHello() {
        return "Hello, friend";
    }
}
