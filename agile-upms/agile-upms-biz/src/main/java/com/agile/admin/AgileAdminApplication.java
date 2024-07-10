package com.agile.admin;

import com.agile.common.security.annotation.EnableAgileResourceServer;
import com.agile.common.swagger.annotation.EnableAgileDoc;
import com.agile.feign.annotation.EnableAgileFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Unified user management system.
 *
 * @author Huang Z.Y.
 */
@EnableAgileDoc(value = "admin")
@EnableAgileFeignClients
@EnableAgileResourceServer
@SpringBootApplication
@EnableDiscoveryClient
public class AgileAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileAdminApplication.class, args);
    }

}
