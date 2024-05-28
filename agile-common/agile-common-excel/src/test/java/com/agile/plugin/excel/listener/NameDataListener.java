package com.agile.plugin.excel.listener;

import com.agile.plugin.excel.read.NameData;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class NameDataListener extends AnalysisEventListener<NameData> {

    @Override
    public void invoke(NameData data, AnalysisContext context) {
        log.info("Parse a piece of data: {}.", data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("All data analysis completed.");
    }

}
    