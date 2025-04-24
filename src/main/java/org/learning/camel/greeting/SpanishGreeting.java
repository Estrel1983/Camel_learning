package org.learning.camel.greeting;

public class SpanishGreeting implements Greeter {
    @Override
    public String sayHello() {
        return "Hola amigo!";
    }
}
