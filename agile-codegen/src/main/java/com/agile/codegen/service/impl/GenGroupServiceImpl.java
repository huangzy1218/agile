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
package com.agile.codegen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.agile.codegen.entity.GenGroupEntity;
import com.agile.codegen.entity.GenTemplateGroupEntity;
import com.agile.codegen.entity.vo.GroupVo;
import com.agile.codegen.entity.vo.TemplateGroupDTO;
import com.agile.codegen.mapper.GenGroupMapper;
import com.agile.codegen.service.GenGroupService;
import com.agile.codegen.service.GenTemplateGroupService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Template group service.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@AllArgsConstructor
public class GenGroupServiceImpl extends ServiceImpl<GenGroupMapper, GenGroupEntity> implements GenGroupService {

    private final GenTemplateGroupService genTemplateGroupService;

    /**
     * Add new template group.
     *
     * @param genTemplateGroup Template group DTO
     */
    @Override
    public void saveGenGroup(TemplateGroupDTO genTemplateGroup) {
        // 1. Save group
        GenGroupEntity groupEntity = new GenGroupEntity();
        BeanUtil.copyProperties(genTemplateGroup, groupEntity);
        baseMapper.insert(groupEntity);
        // 2. Save relation
        List<GenTemplateGroupEntity> goals = new LinkedList<>();
        for (Long TemplateId : genTemplateGroup.getTemplateId()) {
            GenTemplateGroupEntity templateGroup = new GenTemplateGroupEntity();
            templateGroup.setTemplateId(TemplateId).setGroupId(groupEntity.getId());
            goals.add(templateGroup);
        }
        genTemplateGroupService.saveBatch(goals);

    }

    /**
     * Delete by ids.
     *
     * @param ids Group Id list
     */
    @Override
    public void delGroupAndTemplate(Long[] ids) {
        // 1. Delete group
        this.removeBatchByIds(CollUtil.toList(ids));
        // 2. Delete relation
        genTemplateGroupService
                .remove(Wrappers.<GenTemplateGroupEntity>lambdaQuery().in(GenTemplateGroupEntity::getGroupId, ids));
    }

    /**
     * Query by ID.
     *
     * @param id ID
     * @return Group VO
     */
    @Override
    public GroupVo getGroupVoById(Long id) {
        return baseMapper.getGroupVoById(id);
    }

    /**
     * Update based on ID.
     *
     * @param groupVo Group VO
     */
    @Override
    public void updateGroupAndTemplateById(GroupVo groupVo) {
        // 1. Update group entity
        GenGroupEntity groupEntity = new GenGroupEntity();
        BeanUtil.copyProperties(groupVo, groupEntity);
        this.updateById(groupEntity);
        // 2. Update template
        // 2.1 Delete previous template based on id
        genTemplateGroupService.remove(
                Wrappers.<GenTemplateGroupEntity>lambdaQuery().eq(GenTemplateGroupEntity::getGroupId, groupVo.getId()));
        // 2.2 Create a new template group assignment based on ids
        List<GenTemplateGroupEntity> goals = new LinkedList<>();
        for (Long templateId : groupVo.getTemplateId()) {
            goals.add(new GenTemplateGroupEntity().setGroupId(groupVo.getId()).setTemplateId(templateId));
        }
        genTemplateGroupService.saveBatch(goals);
    }

}
