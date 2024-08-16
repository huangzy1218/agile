package com.agile.codegen.service;

import com.agile.codegen.entity.GenGroupEntity;
import com.agile.codegen.entity.vo.GroupVo;
import com.agile.codegen.entity.vo.TemplateGroupDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Template group.
 *
 * @author Huang Z.Y.
 */
public interface GenGroupService extends IService<GenGroupEntity> {

    void saveGenGroup(TemplateGroupDTO genTemplateGroup);

    /**
     * Delete group relationships.
     *
     * @param ids Group IDs.
     */
    void delGroupAndTemplate(Long[] ids);

    /**
     * Query group data.
     *
     * @param id Group ID.
     * @return GroupVo object.
     */
    GroupVo getGroupVoById(Long id);

    /**
     * Update group data.
     *
     * @param groupVo GroupVo object.
     */
    void updateGroupAndTemplateById(GroupVo groupVo);

}

