package com.agile.plugin.excel.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Huang Z.Y.
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {

    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";
    // todo

}
    