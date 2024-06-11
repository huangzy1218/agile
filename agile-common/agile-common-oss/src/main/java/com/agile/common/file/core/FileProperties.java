package com.agile.common.file.core;

import com.agile.common.file.local.LocalFileProperties;
import com.agile.common.file.oss.OssProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * File configuration information.
 *
 * @author Huang Z.Y.
 */
@Data
@ConfigurationProperties(prefix = "file")
@AllArgsConstructor
@NoArgsConstructor
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
    