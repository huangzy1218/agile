package com.agile.admin.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysFile;
import com.agile.admin.service.SysFileService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * File management.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Tag(description = "sys-file", name = "File management")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysFileController {

    private final SysFileService sysFileService;

    /**
     * Paging query.
     *
     * @param page    Pagination object
     * @param sysFile File management object
     * @return
     */
    @Operation(summary = "Paging query", description = "Paging query")
    @GetMapping("/page")
    public R getSysFilePage(@ParameterObject Page page, @ParameterObject SysFile sysFile) {
        LambdaQueryWrapper<SysFile> wrapper = Wrappers.<SysFile>lambdaQuery()
                .like(StrUtil.isNotBlank(sysFile.getOriginal()), SysFile::getOriginal, sysFile.getOriginal());
        return R.ok(sysFileService.page(page, wrapper));
    }

    /**
     * Delete file management by id.
     *
     * @param ids ID list
     * @return R
     */
    @Operation(summary = "Delete file management by id", description = "Delete file management by id")
    @SysLog("Delete file management")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_file_del')")
    public R removeById(@RequestBody Long[] ids) {
        for (Long id : ids) {
            sysFileService.deleteFile(id);
        }
        return R.ok();
    }

    /**
     * Upload files.
     * The file name uses uuid to avoid the "-" symbol in
     * the original file name, which may cause abnormal parsing during downloading.
     *
     * @param file File resource
     * @return R(/ admin / bucketName / filename)
     */
    @PostMapping(value = "/upload")
    public R upload(@RequestPart("file") MultipartFile file) {
        return sysFileService.uploadFile(file);
    }

    /**
     * Get file.
     *
     * @param bucket   Bucket name
     * @param fileName File name
     * @param response
     * @return
     */
    @Inner(false)
    @GetMapping("/{bucket}/{fileName}")
    public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
        sysFileService.getFile(bucket, fileName, response);
    }

    /**
     * Get local resource file.
     *
     * @param fileName File name
     * @param response File response
     */
    @SneakyThrows
    @GetMapping("/local/file/{fileName}")
    public void localFile(@PathVariable String fileName, HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("file/" + fileName);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.copy(resource.getInputStream(), response.getOutputStream());
    }

}
