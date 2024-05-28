package com.agile.plugin.excel.read;

import com.agile.plugin.excel.listener.IndexDataListener;
import com.agile.plugin.excel.listener.NameDataListener;
import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Test read excels.
 *
 * @author Huang Z.Y.
 */
public class ReadExcelTests {

    @Test
    void indexOrNamedRead() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("excel/index.xlsx");
        EasyExcel.read(classPathResource.getInputStream(), IndexData.class, new IndexDataListener())
                .sheet()
                .doRead();
    }

    @Test
    void indexOrNameRead1() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("excel/name.xlsx");
        EasyExcel.read(classPathResource.getInputStream(), NameData.class, new NameDataListener())
                .sheet()
                .doRead();
    }

}
    