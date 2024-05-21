package com.agile.plugin.excel;

import com.agile.plugin.excel.head.I18nHeaderCellWriteHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

/**
 * Excel handler configuration.
 *
 * @author Huang Z.Y.
 */
public class ExcelHandlerConfiguration {

    /**
     * Internationalized processor for excel header.
     *
     * @param messageSource Internationalized source
     */
    @Bean
    @ConditionalOnBean(MessageSource.class)
    @ConditionalOnMissingBean
    public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
        // todo
        return new I18nHeaderCellWriteHandler(messageSource);
    }

}
    