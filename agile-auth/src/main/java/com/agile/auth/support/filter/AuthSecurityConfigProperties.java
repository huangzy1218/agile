package com.agile.auth.support.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Security configuration properties.
 *
 * @author Huang Z.Y.
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("security")
public class AuthSecurityConfigProperties {

    /**
     * Whether it is a microservices architecture.
     */
    private boolean isMicro;

    /**
     * The gateway decrypts the front-end login password key.
     */
    private String encodeKey;

    /**
     * The gateway does not need to verify the verification code on the client.
     */
    private List<String> ignoreClients;

}
    