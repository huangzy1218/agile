package com.agile.common.file.local;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Local file configuration information.
 *
 * @author Huang Z.Y.
 */
@Data
@ConfigurationProperties(prefix = "local")
@AllArgsConstructor
@NoArgsConstructor
public class LocalFileProperties {

    /**
     * Enable or not.
     */
    private boolean enable;

    /**
     * Default path.
     */
    private String basePath;

}
    