package com.agile.daemon.quartz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Scheduled task.
 *
 * @author Huang Z.Y.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Scheduled task")
public class SysJob extends Model<SysJob> {

    private static final long serialVersionUID = 1L;

    /**
     * Task ID.
     */
    @TableId(value = "job_id", type = IdType.ASSIGN_ID)
    private Long jobId;

    /**
     * Task name.
     */
    private String jobName;

    /**
     * Task group name.
     */
    private String jobGroup;

    /**
     * The execution within the group is smooth. The larger the value, the higher the execution priority.
     * The maximum value is 9 and the minimum value is 1.
     */
    private String jobOrder;

    /**
     * 1. java class;
     * 2. spring bean name;
     * 3. rest call;
     * 4. jar call;
     * 9. others
     */
    private String jobType;

    /**
     * When job_type = 3, rest calling address, only supports post protocol;
     * when job_type = 4, jar path; other values are empty.
     */
    private String executePath;

    /**
     * When job_type = 1, the full path of the class;
     * when job_type = 2, the spring bean name;
     * other values are empty.
     */
    private String className;

    /**
     * Task method name.
     */
    private String methodName;

    /**
     * Parameter values.
     */
    private String methodParamsValue;

    /**
     * Cron expression.
     */
    private String cronExpression;

    /**
     * Missed execution strategy:
     * 1. miss cycle, execute immediately;
     * 2. miss cycle, execute once;
     * 3. miss cycle, execute.
     */
    private String misfirePolicy;

    /**
     * 1. Multi-tenant tasks;
     * 2. Non-multi-tenant tasks.
     */
    private String jobTenantType;

    /**
     * Status (0, unpublished; 1, published; 2, running; 3, paused; 4, deleted)
     */
    private String jobStatus;

    /**
     * Status (0 normal 1 abnormal).
     */
    private String jobExecuteStatus;

    /**
     * Creator.
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * Updater.
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * Create time.
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * First execution time.
     */
    private LocalDateTime startTime;

    /**
     * Last execution time.
     */
    private LocalDateTime previousTime;

    /**
     * Next execution time.
     */
    private LocalDateTime nextTime;

    /**
     * Remark information.
     */
    private String remark;

}

