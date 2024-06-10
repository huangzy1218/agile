package com.agile.common.file.local;

import com.agile.common.file.core.FileProperties;
import com.agile.common.file.core.FileTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Local file automatic configuration.
 *
 * @author Huang Z.Y.
 */
@AllArgsConstructor
public class LocalFileAutoConfiguration {

    private final FileProperties properties;

    @Bean
    @ConditionalOnMissingBean(LocalFileTemplate.class)
    @ConditionalOnProperty(name = "file.local.enable", havingValue = "true", matchIfMissing = true)
    public FileTemplate localFileTemplate() {
        return new LocalFileTemplate(properties);
    }

}
    