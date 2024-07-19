package com.agile.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.agile.admin.api.entity.SysPost;
import com.agile.admin.api.vo.PostExcelVO;
import com.agile.admin.mapper.SysPostMapper;
import com.agile.admin.service.SysPostService;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.agile.plugin.excel.vo.ErrorMessage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    /**
     * Import positions.
     *
     * @param excelVOList   Positions list
     * @param bindingResult Error message list
     * @return {@code true} for success
     */
    @SuppressWarnings("unchecked")
    @Override
    public R importPost(List<PostExcelVO> excelVOList, BindingResult bindingResult) {
        // Universal verification to obtain failed data
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // Personalized verification logic
        List<SysPost> postList = this.list();

        // Perform data insertion operations and assemble PostDTO
        for (PostExcelVO excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();
            // Check whether the position name or position code exists
            boolean existPost = postList.stream()
                    .anyMatch(post -> excel.getPostName().equals(post.getPostName())
                            || excel.getPostCode().equals(post.getPostCode()));

            if (existPost) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_POST_NAMEORCODE_EXISTING, excel.getPostName(),
                        excel.getPostCode()));
            }

            // Data legality
            if (CollUtil.isEmpty(errorMsg)) {
                insertExcelPost(excel);
            } else {
                // Data is illegal
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
            }
        }
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok();
    }

    /**
     * Export excel table.
     */
    @Override
    public List<PostExcelVO> listPost() {
        List<SysPost> postList = this.list(Wrappers.emptyWrapper());
        // 转换成execl 对象输出
        return postList.stream().map(post -> {
            PostExcelVO postExcelVO = new PostExcelVO();
            BeanUtil.copyProperties(post, postExcelVO);
            return postExcelVO;
        }).collect(Collectors.toList());
    }

    /**
     * Insert excel Post.
     */
    private void insertExcelPost(PostExcelVO excel) {
        SysPost sysPost = new SysPost();
        BeanUtil.copyProperties(excel, sysPost);
        this.save(sysPost);
    }

}

