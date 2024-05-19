package com.agile.plugin.excel.handler;

import com.agile.plugin.excel.vo.ErrorMessage;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.List;

/**
 * Provide a evert listener that parse Excel data into a list of objects.
 *
 * @author Huang Z.Y.
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

    /**
     * Get a list of objects parsed by excel.
     *
     * @return Collection
     */
    public abstract List<T> getList();

    /**
     * Get the abnormal verification result
     *
     * @return Collection
     */
    public abstract List<ErrorMessage> getErrors();

}
    