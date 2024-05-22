package com.agile.plugin.excel.enhance;

import com.agile.plugin.excel.annotation.ResponseExcel;
import com.agile.plugin.excel.head.HeadGenerator;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;

import javax.servlet.http.HttpServletResponse;

/**
 * Class acts as a no-op implementation of {@link WriterBuilderEnhancer} for use when there is No special need.
 *
 * @author Huang Z.Y.
 */
public class DefaultWriterBuilderEnhancer implements WriterBuilderEnhancer {

    @Override
    public ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response, ResponseExcel responseExcel, String templatePath) {
        // Do noting
        return writerBuilder;
    }

    @Override
    public ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo, String sheetName, Class<?> dataClass, String template, Class<? extends HeadGenerator> headEnhancerClass) {
        // Do noting
        return writerSheetBuilder;
    }

}
    