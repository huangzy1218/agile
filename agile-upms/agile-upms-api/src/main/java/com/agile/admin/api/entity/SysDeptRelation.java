package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Departmental relationship.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Departmental relationship")
@EqualsAndHashCode(callSuper = true)
public class SysDeptRelation extends Model<SysDeptRelation> {

    private static final long serialVersionUID = 2963615974691975459L;

    /**
     * Ancestor node.
     */
    @Schema(description = "Ancestor node")
    private Long ancestor;

    /**
     * Descendant node.
     */
    @Schema(description = "Descendant node")
    private Long descendant;

}
    