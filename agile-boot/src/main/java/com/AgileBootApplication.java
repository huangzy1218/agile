package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Single version initiator, only need to run this module to start the entire system.
 *
 * @author Huang Z.Y.
 */
@SpringBootApplication
public class AgileBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileBootApplication.class, args);
    }
}
    