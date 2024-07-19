package com.agile.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysDept;
import com.agile.admin.api.vo.DeptExcelVO;
import com.agile.admin.mapper.SysDeptMapper;
import com.agile.admin.service.SysDeptService;
import com.agile.common.core.util.R;
import com.agile.plugin.excel.vo.ErrorMessage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Department management service implementation class.
 *
 * @author Huang Z.Y.
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptMapper deptMapper;

    /**
     * Delete department.
     *
     * @param id Department ID
     * @return {@code true} for success
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Long id) {
        // Deleting a department in cascading mode
        List<Long> idList = this.listDescendant(id).stream().map(SysDept::getDeptId).collect(Collectors.toList());

        Optional.of(idList).filter(CollUtil::isNotEmpty).ifPresent(this::removeByIds);

        return Boolean.TRUE;
    }

    /**
     * Query all department trees.
     *
     * @param deptName
     * @return Tree department name
     */
    @Override
    public List<Tree<Long>> selectTree(String deptName) {
        // Search all departments
        List<SysDept> deptAllList = deptMapper
                .selectList(Wrappers.<SysDept>lambdaQuery().like(StrUtil.isNotBlank(deptName), SysDept::getName, deptName));

        // Permissions within the department
        List<TreeNode<Long>> collect = deptAllList.stream()
                .filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
                .sorted(Comparator.comparingInt(SysDept::getSortOrder))
                .map(dept -> {
                    TreeNode<Long> treeNode = new TreeNode();
                    treeNode.setId(dept.getDeptId());
                    treeNode.setParentId(dept.getParentId());
                    treeNode.setName(dept.getName());
                    treeNode.setWeight(dept.getSortOrder());
                    // Do not return ID if you have permission
                    Map<String, Object> extra = new HashMap<>(8);
                    extra.put("createTime", dept.getCreateTime());
                    treeNode.setExtra(extra);
                    return treeNode;
                })
                .collect(Collectors.toList());

        // Fuzzy query does not assemble the tree structure and returns directly. The table is easy to edit
        if (StrUtil.isNotBlank(deptName)) {
            return collect.stream().map(node -> {
                Tree<Long> tree = new Tree<>();
                tree.putAll(node.getExtra());
                BeanUtils.copyProperties(node, tree);
                return tree;
            }).collect(Collectors.toList());
        }

        return TreeUtil.build(collect, 0L);
    }

    /**
     * Export department.
     */
    @Override
    public List<DeptExcelVO> listExcelVo() {
        List<SysDept> list = this.list();
        List<DeptExcelVO> deptExcelVos = list.stream().map(item -> {
            DeptExcelVO deptExcelVo = new DeptExcelVO();
            deptExcelVo.setName(item.getName());
            Optional<String> first = this.list()
                    .stream()
                    .filter(it -> item.getParentId().equals(it.getDeptId()))
                    .map(SysDept::getName)
                    .findFirst();
            deptExcelVo.setParentName(first.orElse("Root department"));
            deptExcelVo.setSortOrder(item.getSortOrder());
            return deptExcelVo;
        }).collect(Collectors.toList());
        return deptExcelVos;
    }


    @Override
    public R importDept(List<DeptExcelVO> excelVOList, BindingResult bindingResult) {
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        List<SysDept> deptList = this.list();
        for (DeptExcelVO item : excelVOList) {
            Set<String> errorMsg = new HashSet<>();
            boolean exsitUsername = deptList.stream().anyMatch(sysDept -> item.getName().equals(sysDept.getName()));
            if (exsitUsername) {
                errorMsg.add("Department name already exists");
            }
            SysDept one = this.getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getName, item.getParentName()));
            if (item.getParentName().equals("Root department")) {
                one = new SysDept();
                one.setDeptId(0L);
            }
            if (one == null) {
                errorMsg.add("The superior department does not exist");
            }
            if (CollUtil.isEmpty(errorMsg)) {
                SysDept sysDept = new SysDept();
                sysDept.setName(item.getName());
                sysDept.setParentId(one.getDeptId());
                sysDept.setSortOrder(item.getSortOrder());
                baseMapper.insert(sysDept);
            } else {
                // Data is illegal
                errorMessageList.add(new ErrorMessage(item.getLineNum(), errorMsg));
            }
        }
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok(null, "Department import successful");
    }

    /**
     * Query all child nodes (including the current node).
     *
     * @param deptId Target department ID
     * @return ID
     */
    @Override
    public List<SysDept> listDescendant(Long deptId) {
        // Search all departments
        List<SysDept> allDeptList = baseMapper.selectList(Wrappers.emptyWrapper());

        // Recursively query all child nodes
        List<SysDept> resDeptList = new ArrayList<>();
        recursiveDept(allDeptList, deptId, resDeptList);

        // Add current node
        resDeptList.addAll(allDeptList.stream()
                .filter(sysDept -> deptId.equals(sysDept.getDeptId()))
                .toList());
        return resDeptList;
    }

    /**
     * Recursively query all child nodes.
     *
     * @param allDeptList List of all departments
     * @param parentId    Parent department ID
     * @param resDeptList Result set
     */
    private void recursiveDept(List<SysDept> allDeptList, Long parentId, List<SysDept> resDeptList) {
        allDeptList.stream().filter(sysDept -> sysDept.getParentId().equals(parentId)).forEach(sysDept -> {
            resDeptList.add(sysDept);
            recursiveDept(allDeptList, sysDept.getDeptId(), resDeptList);
        });
    }

}

    