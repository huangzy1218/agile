package com.agile.test;

import com.agile.common.swagger.annotation.EnableAgileDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Agile test module application.
 *
 * @author Huang Z.Y.
 */
@SpringBootApplication
@EnableAgileDoc("test")
public class AgileTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileTestApplication.class);
    }

}
