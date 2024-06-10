package com.agile.common.file;

import com.agile.common.file.core.FileProperties;
import com.agile.common.file.local.LocalFileAutoConfiguration;
import com.agile.common.file.oss.OssAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * OSS automatic configuration.
 *
 * @author Huang Z.Y.
 */
@Import({LocalFileAutoConfiguration.class, OssAutoConfiguration.class})
@EnableConfigurationProperties({FileProperties.class})
public class FileAutoConfiguration {
}
    