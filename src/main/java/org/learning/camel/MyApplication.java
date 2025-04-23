package org.learning.camel;

import org.apache.camel.main.Main;

/**
 * Main class that boot the Camel application
 */
public final class MyApplication {

    private MyApplication() {
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main(MyApplication.class);
        main.run(args);
    }

}
