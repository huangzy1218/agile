package com.agile.daemon.quartz.controller;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.util.SecurityUtils;
import com.agile.daemon.quartz.constant.AgileQuartzEnum;
import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.entity.SysJobLog;
import com.agile.daemon.quartz.service.SysJobLogService;
import com.agile.daemon.quartz.service.SysJobService;
import com.agile.daemon.quartz.util.TaskExecutor;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-job")
@Tag(description = "sys-job", name = "定时任务")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
@Slf4j
public class SysJobController {

    private final SysJobService sysJobService;

    private final SysJobLogService sysJobLogService;

    private final TaskExecutor executor;

    private final Scheduler scheduler;

    /**
     * Scheduled task paging query.
     *
     * @param page   Pagination object
     * @param sysJob Scheduled task schedule
     * @return R
     */
    @GetMapping("/page")
    @Operation(description = "Paging scheduled business query")
    public R getSysJobPage(Page page, SysJob sysJob) {
        LambdaQueryWrapper<SysJob> wrapper = Wrappers.<SysJob>lambdaQuery()
                .like(StrUtil.isNotBlank(sysJob.getJobName()), SysJob::getJobName, sysJob.getJobName())
                .like(StrUtil.isNotBlank(sysJob.getJobGroup()), SysJob::getJobGroup, sysJob.getJobGroup())
                .eq(StrUtil.isNotBlank(sysJob.getJobStatus()), SysJob::getJobStatus, sysJob.getJobGroup())
                .eq(StrUtil.isNotBlank(sysJob.getJobExecuteStatus()), SysJob::getJobExecuteStatus,
                        sysJob.getJobExecuteStatus());
        return R.ok(sysJobService.page(page, wrapper));
    }

    /**
     * Query scheduled tasks by id.
     *
     * @param id id
     * @return R
     */
    @GetMapping("/{id}")
    @Operation(description = "Unique identifier query scheduled task")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(sysJobService.getById(id));
    }

    /**
     * Add a new scheduled task, the default new status is 1 Published.
     *
     * @param sysJob Scheduled task
     * @return R
     */
    @SysLog("Add a new scheduled task")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('job_sys_job_add')")
    @Operation(description = "Add a new scheduled task")
    public R save(@RequestBody SysJob sysJob) {
        long count = sysJobService.count(
                Wrappers.query(SysJob.builder().jobName(sysJob.getJobName()).jobGroup(sysJob.getJobGroup()).build()));

        if (count > 0) {
            return R.failed("The task is duplicated. " +
                    "Please check whether this group already contains a task with the same name.");
        }
        sysJob.setJobStatus(AgileQuartzEnum.JOB_STATUS_RELEASE.getType());
        sysJob.setCreateBy(SecurityUtils.getUser().getUsername());
        return R.ok(sysJobService.save(sysJob));
    }

    /**
     * Modify scheduled tasks.
     *
     * @param sysJob Scheduled task
     * @return R
     */
    @SysLog("Modify scheduled task")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('job_sys_job_edit')")
    @Operation(description = "Modify scheduled task")
    public R updateById(@RequestBody SysJob sysJob) {
        sysJob.setUpdateBy(SecurityUtils.getUser().getUsername());
        SysJob querySysJob = this.sysJobService.getById(sysJob.getJobId());
        if (AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(querySysJob.getJobStatus())) {
            // If you modify the pause, you need to update the scheduler
            this.executor.addOrUpdateJob(sysJob, scheduler);
            sysJobService.updateById(sysJob);
        } else if (AgileQuartzEnum.JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
            sysJobService.updateById(sysJob);
        }
        return R.ok();
    }

    /**
     * Delete scheduled tasks by id.
     *
     * @param id id
     * @return R
     */
    @SysLog("Delete scheduled task")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('job_sys_job_del')")
    @Operation(description = "Uniquely identifies scheduled tasks and can be deleted only by pausing the task")
    public R removeById(@PathVariable Long id) {
        SysJob querySysJob = this.sysJobService.getById(id);
        if (AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(querySysJob.getJobStatus())) {
            this.executor.removeJob(querySysJob, scheduler);
            this.sysJobService.removeById(id);
        } else if (AgileQuartzEnum.JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
            this.sysJobService.removeById(id);
        }
        return R.ok();
    }

    /**
     * Pause all scheduled tasks.
     *
     * @return R
     */
    @SysLog("Pause all scheduled task")
    @PostMapping("/shutdown-jobs")
    @PreAuthorize("@pms.hasPermission('job_sys_job_shutdown_job')")
    @Operation(description = "Pause all scheduled task")
    public R shutdownJobs() {
        executor.pauseJobs(scheduler);
        long count = this.sysJobService.count(
                new LambdaQueryWrapper<SysJob>().eq(SysJob::getJobStatus, AgileQuartzEnum.JOB_STATUS_RUNNING.getType()));
        if (count <= 0) {
            return R.ok("No scheduled tasks are running.");
        } else {
            // Update the scheduled task status conditions, and update the running status 2 to the paused status 3
            this.sysJobService.update(
                    SysJob.builder().jobStatus(AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType()).build(),
                    new UpdateWrapper<SysJob>().lambda()
                            .eq(SysJob::getJobStatus, AgileQuartzEnum.JOB_STATUS_RUNNING.getType()));
            return R.ok("Suspended successfully");
        }
    }

    /**
     * Start all scheduled tasks.
     */
    @SysLog("Start all scheduled tasks")
    @PostMapping("/start-jobs")
    @PreAuthorize("@pms.hasPermission('job_sys_job_start_job')")
    @Operation(description = "Start all paused scheduled tasks")
    public R startJobs() {
        // Update the scheduled task status conditions, and update the paused status 3 to running status 2
        this.sysJobService.update(SysJob.builder().jobStatus(AgileQuartzEnum.JOB_STATUS_RUNNING.getType()).build(),
                new UpdateWrapper<SysJob>().lambda()
                        .eq(SysJob::getJobStatus, AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType()));
        executor.startJobs(scheduler);
        return R.ok();
    }

    /**
     * Refresh all scheduled tasks. Pause and running tasks are added to the scheduler.
     * Other states are removed from the scheduler.
     *
     * @return R
     */
    @SysLog("Refresh all scheduled task")
    @PostMapping("/refresh-jobs")
    @PreAuthorize("@pms.hasPermission('job_sys_job_refresh_job')")
    @Operation(description = "Refresh all scheduled task")
    public R refreshJobs() {
        sysJobService.list().forEach(sysjob -> {
            if (AgileQuartzEnum.JOB_STATUS_RUNNING.getType().equals(sysjob.getJobStatus())
                    || AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(sysjob.getJobStatus())) {
                executor.addOrUpdateJob(sysjob, scheduler);
            } else {
                executor.removeJob(sysjob, scheduler);
            }
        });
        return R.ok();
    }

    /**
     * Start a scheduled task.
     *
     * @param jobId Job id
     * @return R
     */
    @SysLog("Start a scheduled task")
    @PostMapping("/start-job/{id}")
    @PreAuthorize("@pms.hasPermission('job_sys_job_start_job')")
    @Operation(description = "Start a scheduled task")
    public R startJob(@PathVariable("id") Long jobId) throws SchedulerException {
        SysJob querySysJob = this.sysJobService.getById(jobId);
        if (querySysJob == null) {
            return R.failed("There is no such scheduled task, please confirm");
        }

        // If the scheduled task does not exist, the forced status is 1 Published
        if (!scheduler.checkExists(executor.getJobKey(querySysJob))) {
            querySysJob.setJobStatus(AgileQuartzEnum.JOB_STATUS_RELEASE.getType());
            log.warn("The scheduled task is not in quartz, task id: {}, " +
                    "the forced status is published and added to the scheduler", jobId);
        }

        if (AgileQuartzEnum.JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
            executor.addOrUpdateJob(querySysJob, scheduler);
        } else {
            executor.resumeJob(querySysJob, scheduler);
        }
        // Update the scheduled task status to running status 2
        this.sysJobService
                .updateById(SysJob.builder().jobId(jobId).jobStatus(AgileQuartzEnum.JOB_STATUS_RUNNING.getType()).build());
        return R.ok();
    }

    /**
     * Start a scheduled task.
     *
     * @param jobId Job id
     * @return R
     */
    @SysLog("Execute scheduled tasks immediately")
    @PostMapping("/run-job/{id}")
    @PreAuthorize("@pms.hasPermission('job_sys_job_run_job')")
    @Operation(description = "Execute scheduled tasks immediately")
    public R runJob(@PathVariable("id") Long jobId) throws SchedulerException {
        SysJob querySysJob = this.sysJobService.getById(jobId);

        // Determine whether the task is in quartz before executing the scheduled task
        if (!scheduler.checkExists(executor.getJobKey(querySysJob))) {
            querySysJob.setJobStatus(AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType());
            log.warn("Execute the scheduled task immediately - the scheduled task is not in quartz, task id: {}, " +
                    "the forced status is paused and added to the scheduler", jobId);
            executor.addOrUpdateJob(querySysJob, scheduler);
        }

        return TaskExecutor.runOnce(scheduler, querySysJob) ? R.ok() : R.failed();
    }

    /**
     * Pause scheduled tasks.
     */
    @SysLog("Pause scheduled task")
    @PostMapping("/shutdown-job/{id}")
    @PreAuthorize("@pms.hasPermission('job_sys_job_shutdown_job')")
    @Operation(description = "Pause scheduled task")
    public R shutdownJob(@PathVariable("id") Long id) {
        SysJob querySysJob = this.sysJobService.getById(id);
        // Update the scheduled task status conditions, and update the running status 2 to the paused status 3
        this.sysJobService.updateById(SysJob.builder()
                .jobId(querySysJob.getJobId())
                .jobStatus(AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType())
                .build());
        executor.pauseJob(querySysJob, scheduler);
        return R.ok();
    }

    /**
     * Unique identifier query scheduled execution log.
     */
    @GetMapping("/job-log")
    @Operation(description = "Unique identifier query scheduled execution log")
    public R getJobLog(Page page, SysJobLog sysJobLog) {
        return R.ok(sysJobLogService.page(page, Wrappers.query(sysJobLog)));
    }

    /**
     * Verify that the task name and task group union are unique.
     *
     * @return
     */
    @GetMapping("/is-valid-task-name")
    @Operation(description = "Verify that the task name and task group union are unique")
    public R isValidTaskName(@RequestParam String jobName, @RequestParam String jobGroup) {
        return this.sysJobService
                .count(Wrappers.query(SysJob.builder().jobName(jobName).jobGroup(jobGroup).build())) > 0
                ? R.failed("The task is duplicated. " +
                "Please check whether this group already contains a task with the same name.") : R.ok();
    }

    /**
     * Export task.
     *
     * @param sysJob
     * @return
     */
    @ResponseExcel
    @GetMapping("/export")
    @Operation(description = "Export job")
    public List<SysJob> export(SysJob sysJob) {
        return sysJobService.list(Wrappers.query(sysJob));
    }

}
