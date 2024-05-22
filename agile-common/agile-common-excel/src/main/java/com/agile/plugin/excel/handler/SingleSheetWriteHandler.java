package com.agile.plugin.excel.handler;

import com.agile.plugin.excel.annotation.ResponseExcel;
import com.agile.plugin.excel.config.ExcelConfigProperties;
import com.agile.plugin.excel.enhance.WriterBuilderEnhancer;
import com.agile.plugin.excel.util.ExcelException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Handle single sheet pages.
 *
 * @author Huang Z.Y.
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

    public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
                                   ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
        super(configProperties, converterProvider, excelWriterBuilderEnhance);
    }

    /**
     * Return true only if obj is a List and list is not empty and the element in list is not a List.
     *
     * @param obj The object to judge Return object
     * @return boolean
     */
    @Override
    public boolean support(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && !(objList.get(0) instanceof List);
        } else {
            throw new ExcelException("The @ResponseExcel return value must be of type List.");
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> eleList = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

        WriteSheet sheet;
        if (CollectionUtils.isEmpty(eleList)) {
            sheet = EasyExcel.writerSheet(responseExcel.sheets()[0].sheetName()).build();
        } else {
            // If a template exists, the sheet name is not specified
            Class<?> dataClass = eleList.get(0).getClass();
            sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(),
                    responseExcel.headGenerator());
        }

        if (responseExcel.fill()) {
            // Fill sheet
            excelWriter.fill(eleList, sheet);
        } else {
            // Write sheet
            excelWriter.write(eleList, sheet);
        }
        excelWriter.finish();
    }

}
    