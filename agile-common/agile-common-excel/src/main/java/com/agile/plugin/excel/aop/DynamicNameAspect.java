package com.agile.plugin.excel.aop;

import com.agile.plugin.excel.annotation.ResponseExcel;
import com.agile.plugin.excel.processor.NameProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Huang Z.Y.
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {

    /**
     * Constant key used to store the Excel name in the request attributes.
     */
    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

    /**
     * Processor for determining the Excel file name based on SpEL expressions.
     */
    private final NameProcessor processor;

    /**
     * This method is executed before any method annotated with @ResponseExcel.
     * It determines the Excel file name to be used, based on the annotation value or the current timestamp.
     * The determined name is then stored in the request attributes for later use.
     *
     * @param point The join point representing the method execution
     * @param excel The ResponseExcel annotation on the method
     */
    @Before("@annotation(excel)")
    public void around(JoinPoint point, ResponseExcel excel) {
        // Get the method signature from the join point
        MethodSignature ms = (MethodSignature) point.getSignature();

        // Get the name specified in the ResponseExcel annotation
        String name = excel.name();

        // If no name is specified, use the current timestamp as the name
        if (!StringUtils.hasText(name)) {
            name = LocalDateTime.now().toString();
        } else {
            // Otherwise, use the NameProcessor to determine the name based on SPEL expressions
            name = processor.doDetermineName(point.getArgs(), ms.getMethod(), excel.name());
        }

        // Get the current request attributes and store the determined name in the request scope
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, RequestAttributes.SCOPE_REQUEST);
    }
    
}
    