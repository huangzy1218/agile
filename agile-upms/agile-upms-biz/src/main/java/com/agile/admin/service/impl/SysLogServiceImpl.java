package com.agile.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.dto.SysLogDTO;
import com.agile.admin.api.entity.SysLog;
import com.agile.admin.mapper.SysLogMapper;
import com.agile.admin.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Log table service implementation class.
 *
 * @author Huang Z.Y.
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public Page getLogByPage(Page page, SysLogDTO sysLog) {
        return baseMapper.selectPage(page, buildQuery(sysLog));
    }

    /**
     * Insert log.
     *
     * @param sysLog Log object
     * @return {@code true} for success
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveLog(SysLog sysLog) {
        baseMapper.insert(sysLog);
        return Boolean.TRUE;
    }

    /**
     * Query log list.
     *
     * @param sysLog Query conditions
     */
    @Override
    public List<SysLog> getList(SysLogDTO sysLog) {
        return baseMapper.selectList(buildQuery(sysLog));
    }

    /**
     * Build query conditions.
     *
     * @param sysLog Query conditions
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper buildQuery(SysLogDTO sysLog) {
        LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(sysLog.getLogType())) {
            wrapper.eq(SysLog::getLogType, sysLog.getLogType());
        }

        if (ArrayUtil.isNotEmpty(sysLog.getCreateTime())) {
            wrapper.ge(SysLog::getCreateTime, sysLog.getCreateTime()[0])
                    .le(SysLog::getCreateTime, sysLog.getCreateTime()[1]);
        }

        return wrapper;
    }

}

