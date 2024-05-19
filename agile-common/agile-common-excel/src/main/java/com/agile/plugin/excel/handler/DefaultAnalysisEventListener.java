package com.agile.plugin.excel.handler;

import com.agile.plugin.excel.annotation.ExcelLine;
import com.agile.plugin.excel.kit.Validators;
import com.agile.plugin.excel.vo.ErrorMessage;
import com.alibaba.excel.context.AnalysisContext;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default {@link ListAnalysisEventListener}.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

    private final List<Object> list = new ArrayList<>();

    private final List<ErrorMessage> errorMessageList = new ArrayList<>();

    private Long lineNum = 1L;

    /**
     * Callback method invoked for each row of data parsed from the Excel file during analysis.
     *
     * @param o               The parsed data object representing a row of Excel data
     * @param analysisContext The analysis context
     */
    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        lineNum++;
        // Validate the parsed data object
        Set<ConstraintViolation<Object>> violations = Validators.validate(o);
        if (!violations.isEmpty()) {
            // Validate failed
            Set<String> messageSet = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            errorMessageList.add(new ErrorMessage(lineNum, messageSet));
        } else {
            // Set line number value for fields annotated with @ExcelLine and of type Long
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                // LineNum field assign value
                if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Long.class) {
                    try {
                        field.setAccessible(true);
                        field.set(o, lineNum);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(o);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("Excel read analysed");
    }

    @Override
    public List<Object> getList() {
        return list;
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return errorMessageList;
    }

}
    