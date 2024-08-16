package com.agile.codegen.entity.vo;

import com.agile.codegen.entity.GenTemplateEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Data
public class GroupVo {

    /**
     * Id.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "Id")
    private Long id;

    /**
     * Group name.
     */
    @Schema(description = "Group name")
    private String groupName;

    /**
     * Group description.
     */
    @Schema(description = "Group description")
    private String groupDesc;

    /**
     * Template ids.
     */
    @Schema(description = "List of template ids")
    private Long[] templateId;

    /**
     * Template list.
     */
    @Schema(description = "List of templates")
    private List<GenTemplateEntity> templateList;

}
