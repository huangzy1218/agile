package com.agile.common.file.oss;

import com.agile.common.file.core.FileProperties;
import com.agile.common.file.core.FileTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * AWS automatic configuration.
 *
 * @author Huang Z.Y.
 */
@EnableConfigurationProperties({OssProperties.class})
@AllArgsConstructor
public class OssAutoConfiguration {

    private final FileProperties properties;

    @Bean
    @Primary
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnProperty(name = "file.oss.enable", havingValue = "true")
    public FileTemplate ossTemplate() {
        return new OssTemplate(properties);
    }

}
    