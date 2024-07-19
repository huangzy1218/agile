package com.agile.admin.service;

import com.agile.admin.api.entity.SysPost;
import com.agile.admin.api.vo.PostExcelVO;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Position information form.
 *
 * @author Huang Z.Y.
 */
public interface SysPostService extends IService<SysPost> {

    /**
     * Export excel table.
     */
    List<PostExcelVO> listPost();

    /**
     * Import positions.
     *
     * @param excelVOList   Job list.
     * @param bindingResult Error message list.
     * @return {@code true} for success
     */
    R importPost(List<PostExcelVO> excelVOList, BindingResult bindingResult);

}
