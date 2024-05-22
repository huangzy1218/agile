package com.agile.plugin.excel.config;

import com.agile.plugin.excel.aop.ResponseExcelReturnValueHandler;
import com.agile.plugin.excel.enhance.DefaultWriterBuilderEnhancer;
import com.agile.plugin.excel.enhance.WriterBuilderEnhancer;
import com.agile.plugin.excel.handler.MultiSheetWriteHandler;
import com.agile.plugin.excel.handler.SheetWriteHandler;
import com.agile.plugin.excel.handler.SingleSheetWriteHandler;
import com.agile.plugin.excel.head.I18nHeaderCellWriteHandler;
import com.alibaba.excel.converters.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Excel handler configuration.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class ExcelHandlerConfiguration {

    private final ExcelConfigProperties configProperties;

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    /**
     * {@link com.alibaba.excel.write.ExcelBuilder} enhancement.
     *
     * @return DefaultWriterBuilderEnhancer An enhancer that does nothing by default
     */
    @Bean
    @ConditionalOnMissingBean
    public WriterBuilderEnhancer writerBuilderEnhancer() {
        return new DefaultWriterBuilderEnhancer();
    }

    /**
     * Single sheet write processor.
     */
    @Bean
    @ConditionalOnMissingBean
    public SingleSheetWriteHandler singleSheetWriteHandler() {
        return new SingleSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer());
    }

    /**
     * Multi-sheet write processor.
     */
    @Bean
    @ConditionalOnMissingBean
    public MultiSheetWriteHandler manySheetWriteHandler() {
        return new MultiSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer());
    }

    /**
     * Return the response handler for the Excel file.
     *
     * @param sheetWriteHandlerList 页签写入处理器集合
     * @return ResponseExcelReturnValueHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
            List<SheetWriteHandler> sheetWriteHandlerList) {
        return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
    }

    /**
     * Internationalized processor for excel header.
     *
     * @param messageSource Internationalized source
     */
    @Bean
    @ConditionalOnBean(MessageSource.class)
    @ConditionalOnMissingBean
    public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
        return new I18nHeaderCellWriteHandler(messageSource);
    }

}
    