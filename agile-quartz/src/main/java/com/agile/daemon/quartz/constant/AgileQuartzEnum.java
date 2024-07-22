package com.agile.daemon.quartz.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public enum AgileQuartzEnum {

    /**
     * Missed execution policy default.
     */
    MISFIRE_DEFAULT("0", "Default"),

    /**
     * Missed execution strategy - execute missed tasks immediately.
     */
    MISFIRE_IGNORE_MISFIRES("1", "Execute missed tasks immediately"),

    /**
     * Missed execution strategy - triggers an execution cycle execution.
     */
    MISFIRE_FIRE_AND_PROCEED("2", "Trigger an execution cycle execution"),

    /**
     * Missed execution strategy-does not trigger execution cycle execution.
     */
    MISFIRE_DO_NOTHING("3", "Does not trigger periodic execution"),

    /**
     * Key of task details.
     */
    SCHEDULE_JOB_KEY("scheduleJob", "Get the key of task details"),

    /**
     * JOB execution status: 0 execution successful.
     */
    JOB_LOG_STATUS_SUCCESS("0", "Execution succeed"),
    /**
     * JOB execution status: 1 execution failed.
     */
    JOB_LOG_STATUS_FAIL("1", "Execution failed"),

    /**
     * JOB status: 1 Published
     */
    JOB_STATUS_RELEASE("1", "Published"),
    /**
     * JOB status: 2 Running
     */
    JOB_STATUS_RUNNING("2", "Running"),
    /**
     * JOB status: 3 Suspended
     */
    JOB_STATUS_NOT_RUNNING("3", "Suspended"),
    /**
     * JOB status: 4 Deleted
     */
    JOB_STATUS_DEL("4", "Deleted");

    /**
     * Type.
     */
    private final String type;

    /**
     * Description.
     */
    private final String description;

}
