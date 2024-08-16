package com.agile.codegen.entity.vo;

import com.agile.codegen.entity.GenGroupEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Template transfer object")
@EqualsAndHashCode(callSuper = true)
public class TemplateGroupDTO extends GenGroupEntity {

    /**
     * Template ID collection.
     */
    @Schema(description = "Template id collection")
    private List<Long> templateId;

}
