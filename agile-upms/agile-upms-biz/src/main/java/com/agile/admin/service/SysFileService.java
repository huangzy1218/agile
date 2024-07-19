package com.agile.admin.service;

/**
 * @author Huang Z.Y.
 */

import com.agile.admin.api.entity.SysFile;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * File management.
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * Upload file.
     *
     * @param file File object
     */
    R uploadFile(MultipartFile file);

    /**
     * Get file.
     *
     * @param bucket   Bucket name
     * @param fileName File name
     * @param response Output stream
     */
    void getFile(String bucket, String fileName, HttpServletResponse response);

    /**
     * Delete file
     *
     * @param id File id
     */
    Boolean deleteFile(Long id);

}

