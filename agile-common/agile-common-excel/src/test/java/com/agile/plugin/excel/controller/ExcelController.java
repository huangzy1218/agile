package com.agile.plugin.excel.controller;

import com.agile.plugin.excel.annotation.RequestExcel;
import com.agile.plugin.excel.read.IndexData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ExcelController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestExcel List<IndexData> dataList) {
        return ResponseEntity.ok(null);
    }
}
