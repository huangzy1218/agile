package com.agile.common.file.core;

import com.agile.common.file.local.LocalFileProperties;
import com.agile.common.file.oss.OssProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * File configuration information.
 *
 * @author Huang Z.Y.
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /**
     * Default bucket name.
     */
    private String bucketName = "local";

    /**
     * Local file configuration information.
     */
    private LocalFileProperties local;

    /**
     * OSS file configuration information.
     */
    private OssProperties oss;

}
    