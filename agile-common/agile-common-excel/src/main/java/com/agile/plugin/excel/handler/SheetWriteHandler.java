package com.agile.plugin.excel.handler;

import com.agile.plugin.excel.annotation.ResponseExcel;

import javax.servlet.http.HttpServletResponse;

/**
 * Defines the processor that exports the data to Excel files.
 *
 * @author Huang Z.Y.
 */
public interface SheetWriteHandler {

    /**
     * Support or not.
     *
     * @param obj The object to judge
     * @return Whether or not
     */
    boolean support(Object obj);

    /**
     * Verify that the configuration in the annotation is valid.
     *
     * @param responseExcel Annotation.
     */
    void check(ResponseExcel responseExcel);

    /**
     * Export data as an Excel file.
     *
     * @param o             Object
     * @param response      Output object
     * @param responseExcel Annotation
     */
    void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

    /**
     * Write the object to an Excel file.
     *
     * @param o             Object
     * @param response      Output object
     * @param responseExcel Annotation
     */
    void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);

}
    