package com.agile.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway application.
 *
 * @author Huang Z.Y.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AgileGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileGatewayApplication.class, args);
    }

}
    