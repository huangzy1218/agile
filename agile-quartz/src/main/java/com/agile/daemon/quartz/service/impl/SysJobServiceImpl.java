package com.agile.daemon.quartz.service.impl;

import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.mapper.SysJobMapper;
import com.agile.daemon.quartz.service.SysJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Scheduled task service implementation class.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysJobService {
}
