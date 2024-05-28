package com.agile.plugin.excel.listener;

import com.agile.plugin.excel.read.IndexData;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Read Excel core processing classes.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class IndexDataListener extends AnalysisEventListener<IndexData> {

    @Override
    public void invoke(IndexData data, AnalysisContext context) {
        log.info("Parse a piece of data: {}.", data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("All data analysis completed.");
    }

}
    