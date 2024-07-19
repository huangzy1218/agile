package com.agile.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysFile;
import com.agile.admin.mapper.SysFileMapper;
import com.agile.admin.service.SysFileService;
import com.agile.common.core.util.R;
import com.agile.common.file.core.FileProperties;
import com.agile.common.file.core.FileTemplate;
import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    private final FileTemplate fileTemplate;

    private final FileProperties properties;

    @Override
    public R uploadFile(MultipartFile file) {
        String fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", properties.getBucketName());
        resultMap.put("fileName", fileName);
        resultMap.put("url", String.format("/admin/sys-file/%s/%s", properties.getBucketName(), fileName));

        try (InputStream inputStream = file.getInputStream()) {
            fileTemplate.putObject(properties.getBucketName(), fileName, inputStream, file.getContentType());
            // File management data records, collection management tracking files
            fileLog(file, fileName);
        } catch (Exception e) {
            log.error("Upload failed", e);
            return R.failed(e.getLocalizedMessage());
        }
        return R.ok(resultMap);
    }

    @Override
    public void getFile(String bucket, String fileName, HttpServletResponse response) {
        try (S3Object s3Object = fileTemplate.getObject(bucket, fileName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        } catch (Exception e) {
            log.error("File reading exception: {}", e.getLocalizedMessage());
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFile(Long id) {
        SysFile file = this.getById(id);
        if (Objects.isNull(file)) {
            return Boolean.FALSE;
        }
        fileTemplate.removeObject(properties.getBucketName(), file.getFileName());
        return this.removeById(id);
    }

    /**
     * File management data records, collection management tracking files.
     *
     * @param file     Upload file format
     * @param fileName File name
     */
    private void fileLog(MultipartFile file, String fileName) {
        SysFile sysFile = new SysFile();
        sysFile.setFileName(fileName);
        sysFile.setOriginal(file.getOriginalFilename());
        sysFile.setFileSize(file.getSize());
        sysFile.setType(FileUtil.extName(file.getOriginalFilename()));
        sysFile.setBucketName(properties.getBucketName());
        this.save(sysFile);
    }

}

