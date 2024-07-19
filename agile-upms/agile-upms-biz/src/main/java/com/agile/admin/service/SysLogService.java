package com.agile.admin.service;

import com.agile.admin.api.dto.SysLogDTO;
import com.agile.admin.api.entity.SysLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Log service.
 *
 * @author Huang Z.Y.
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * Paging query log.
     *
     * @param page   Page object
     * @param sysLog System log dto
     */
    Page getLogByPage(Page page, SysLogDTO sysLog);

    /**
     * Insert log.
     *
     * @param sysLog Log object
     * @return {@code true} for success
     */
    Boolean saveLog(SysLog sysLog);

    /**
     * Query log list
     *
     * @param sysLog Query conditions
     * @return List<SysLog> Log list
     */
    List<SysLog> getList(SysLogDTO sysLog);

}

