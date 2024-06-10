package com.agile.auth;

import com.agile.feign.annotation.EnableAgileFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Certification and authorization center.
 *
 * @author Huang Z.Y.
 */
@EnableAgileFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AgileAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileAuthApplication.class, args);
    }

}
    