package com.agile.daemon.quartz.entity;

/**
 * @author Huang Z.Y.
 */

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
 * Scheduled task log.
 *
 * @author frwcloud
 * @date 2019-01-27 13:40:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Scheduled task log")
public class SysJobLog extends Model<SysJobLog> {

    private static final long serialVersionUID = 1L;

    /**
     * Task log ID.
     */
    @TableId(value = "job_log_id", type = IdType.ASSIGN_ID)
    private Long jobLogId;

    /**
     * Task ID.
     */
    private Long jobId;

    /**
     * Job name.
     */
    private String jobName;

    /**
     * Job group name.
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
     * when job_type = 4, jar path; other values are empty
     */
    private String executePath;

    /**
     * When job_type = 1, the full path of the class;
     * when job_type = 2, the spring bean name;
     * other values are empty
     */
    private String className;

    /**
     * Task method name.
     */
    private String methodName;

    /**
     * Parameter value.
     */
    private String methodParamsValue;

    /**
     * Cron expression.
     */
    private String cronExpression;

    /**
     * Job log message.
     */
    private String jobMessage;

    /**
     * Execution status (0 normal, 1 failed)
     */
    private String jobLogStatus;

    /**
     * Execute time.
     */
    private String executeTime;

    /**
     * Exception information.
     */
    private String exceptionInfo;

    /**
     * Create time.
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}

