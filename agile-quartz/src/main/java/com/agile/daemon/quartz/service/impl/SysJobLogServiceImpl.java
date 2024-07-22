package com.agile.daemon.quartz.service.impl;

import com.agile.daemon.quartz.entity.SysJobLog;
import com.agile.daemon.quartz.mapper.SysJobLogMapper;
import com.agile.daemon.quartz.service.SysJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Scheduled task log service implementation class.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements SysJobLogService {
}
