package com.agile.plugin.excel.enhance;

import com.agile.plugin.excel.annotation.ResponseExcel;
import com.agile.plugin.excel.head.HeadGenerator;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;

import javax.servlet.http.HttpServletResponse;

/**
 * {@link com.alibaba.excel.write.builder.ExcelWriterBuilder} enhancement.
 *
 * @author Huang Z.Y.
 */
public interface WriterBuilderEnhancer {

    /**
     * {@link ExcelWriterBuilder} enhancement.
     *
     * @param writerBuilder ExcelWriterBuilder
     * @param response      HttpServletResponse
     * @param responseExcel ResponseExcel
     * @param templatePath  Template path.
     * @return ExcelWriterBuilder
     */
    ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
                                    ResponseExcel responseExcel, String templatePath);

    /**
     * {@link ExcelWriterSheetBuilder} enhancement.
     *
     * @param writerSheetBuilder ExcelWriterSheetBuilder
     * @param sheetNo            Sheet number.
     * @param sheetName          Sheet name. Empty if a template exists
     * @param dataClass          The class to which the data currently written belongs
     * @param template           Template file.
     * @param headEnhancerClass  The currently specified custom header processor
     * @return ExcelWriterSheetBuilder
     */
    ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo, String sheetName,
                                         Class<?> dataClass, String template, Class<? extends HeadGenerator> headEnhancerClass);

}
