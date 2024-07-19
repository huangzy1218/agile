package com.agile.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.agile.admin.api.entity.SysPublicParam;
import com.agile.admin.mapper.SysPublicParamMapper;
import com.agile.admin.service.SysPublicParamService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.enumeration.DictTypeEnum;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Public parameter configuration service implementation.
 *
 * @author Huang Z.Y.
 */
@Service
@AllArgsConstructor
public class SysPublicParamServiceImpl extends ServiceImpl<SysPublicParamMapper, SysPublicParam>
        implements SysPublicParamService {

    @Override
    @Cacheable(value = CacheConstants.PARAMS_DETAILS, key = "#publicKey", unless = "#result == null ")
    public String getSysPublicParamKeyToValue(String publicKey) {
        SysPublicParam sysPublicParam = this.baseMapper
                .selectOne(Wrappers.<SysPublicParam>lambdaQuery().eq(SysPublicParam::getPublicKey, publicKey));

        if (sysPublicParam != null) {
            return sysPublicParam.getPublicValue();
        }
        return null;
    }

    @Override
    @CacheEvict(value = CacheConstants.PARAMS_DETAILS, key = "#sysPublicParam.publicKey")
    public R updateParam(SysPublicParam sysPublicParam) {
        SysPublicParam param = this.getById(sysPublicParam.getPublicId());
        // System built-in
        if (DictTypeEnum.SYSTEM.getType().equals(param.getSystemFlag())) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_PARAM_DELETE_SYSTEM));
        }
        return R.ok(this.updateById(sysPublicParam));
    }

    @Override
    @CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
    public R removeParamByIds(Long[] publicIds) {
        List<Long> idList = this.baseMapper.selectBatchIds(CollUtil.toList(publicIds))
                .stream()
                .filter(p -> !p.getSystemFlag().equals(DictTypeEnum.SYSTEM.getType()))// 系统内置的跳过不能删除
                .map(SysPublicParam::getPublicId)
                .collect(Collectors.toList());
        return R.ok(this.removeBatchByIds(idList));
    }

    @Override
    @CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
    public R syncParamCache() {
        return R.ok();
    }

}

