package com.agile.common.file.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OSS configuration.
 *
 * @author Huang Z.Y.
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    /**
     * URL of the object storage service.
     */
    private String endpoint;

    /**
     * Customize the domain name.
     */
    private String customDomain;

    /**
     * Application ID,
     */
    private String appId;

    /**
     * Region.
     */
    private String region;

    /**
     * Access key, identify account.
     */
    private String accessKey;

    /**
     * Secret key, account password.
     */
    private String secretKey;

    /**
     * Maximum number of threads, default is 100.
     */
    private Integer maxConnections = 100;

}
    