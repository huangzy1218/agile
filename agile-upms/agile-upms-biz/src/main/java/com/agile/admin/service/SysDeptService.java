package com.agile.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.agile.admin.api.entity.SysDept;
import com.agile.admin.api.vo.DeptExcelVO;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Department management service.
 *
 * @author Huang Z.Y.
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * Query the department tree menu.
     *
     * @param deptName Department name
     * @return Tree
     */
    List<Tree<Long>> selectTree(String deptName);

    /**
     * Delete department.
     *
     * @param id Department ID
     * @return {@code true} for success
     */
    Boolean removeDeptById(Long id);

    List<DeptExcelVO> listExcelVo();

    R importDept(List<DeptExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * Gets a list of all descendant departments of a department.
     *
     * @param deptId Department ID
     * @return List of descendant departments
     */
    List<SysDept> listDescendant(Long deptId);

}
