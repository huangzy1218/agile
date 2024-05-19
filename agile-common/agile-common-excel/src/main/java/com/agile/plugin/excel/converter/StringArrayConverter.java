package com.agile.plugin.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * LocalDate and string converter.
 *
 * @author Huang Z.Y.
 */
public enum StringArrayConverter implements Converter<String[]> {

    /**
     * Instance.
     */
    INSTANCE;

    @Override
    public Class<?> supportJavaTypeKey() {
        return String[].class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String[] convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                      GlobalConfiguration globalConfiguration) throws ParseException {
        return cellData.getStringValue().split(",");
    }

    @Override
    public WriteCellData<String> convertToExcelData(String[] value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(Arrays.stream(value).collect(Collectors.joining()));
    }

}
    