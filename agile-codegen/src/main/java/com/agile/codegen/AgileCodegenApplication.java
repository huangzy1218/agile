package com.agile.codegen;

import com.agile.common.feign.annotation.EnableAgileFeignClients;
import com.agile.common.security.annotation.EnableAgileResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Huang Z.Y.
 */
@EnableDynamicDataSource
@EnableAgileFeignClients
@EnableDiscoveryClient
@EnableAgileResourceServer
@SpringBootApplication
public class AgileCodegenApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileCodegenApplication.class, args);

    }

}
