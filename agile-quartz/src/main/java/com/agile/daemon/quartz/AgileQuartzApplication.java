package com.agile.daemon.quartz;

import com.agile.common.feign.annotation.EnableAgileFeignClients;
import com.agile.common.swagger.annotation.EnableAgileDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Huang Z.Y.
 */
@EnableAgileDoc("job")
@EnableAgileFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AgileQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileQuartzApplication.class, args);
    }

}
