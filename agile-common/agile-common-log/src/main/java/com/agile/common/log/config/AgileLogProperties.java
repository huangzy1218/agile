package com.agile.common.log.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Log configuration class.
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@ConfigurationProperties(AgileLogProperties.PREFIX)
public class AgileLogProperties {

    public static final String PREFIX = "security.log";

    /**
     * Enable logging.
     */
    private boolean enabled = true;

    /**
     * Release field, such as (default) password, mobile, id card, phone
     */
    @Value("${security.log.exclude-fields:password,mobile,idcard,phone}")
    private List<String> excludeFields;

    /**
     * Maximum storage length of the request packet.
     */
    private Integer maxLength = 2000;
}