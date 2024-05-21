package com.agile.plugin.excel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Specifies the path to the Excel template. The default value is "excel".
 *
 * @author Huang Z.Y.
 */
@Data
@ConfigurationProperties(prefix = ExcelConfigProperties.PREFIX)
public class ExcelConfigProperties {

    static final String PREFIX = "excel";

    /**
     * Template path.
     */
    private String templatePath = "excel";

}
    