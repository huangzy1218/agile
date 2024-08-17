/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.agile.codegen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.codegen.service.GeneratorService;
import com.agile.common.core.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Code generator controller.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
public class GeneratorController {

    private final GeneratorService generatorService;

    /**
     * ZIP download generated code.
     *
     * @param tableIds Data table ID
     * @param response Stream output object
     */
    @SneakyThrows
    @GetMapping("/download")
    public void download(String tableIds, HttpServletResponse response) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        // Generate code
        for (String tableId : tableIds.split(StrUtil.COMMA)) {
            generatorService.downloadCode(Long.parseLong(tableId), zip);
        }

        IoUtil.close(zip);

        // Zip compressed package data
        byte[] data = outputStream.toByteArray();

        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.zip", tableIds));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }

    /**
     * Target directory generates code.
     */
    @ResponseBody
    @GetMapping("/code")
    public R<String> code(String tableIds) throws Exception {
        // Generate code
        for (String tableId : tableIds.split(StrUtil.COMMA)) {
            generatorService.generatorCode(Long.valueOf(tableId));
        }

        return R.ok();
    }

    /**
     * Preview code
     *
     * @param tableId Table ID
     * @return Template names and rendered results
     */
    @SneakyThrows
    @GetMapping("/preview")
    public List<Map<String, String>> preview(Long tableId) {
        return generatorService.preview(tableId);
    }

}
