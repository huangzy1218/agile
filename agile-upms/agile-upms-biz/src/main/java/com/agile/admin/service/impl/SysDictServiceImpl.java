package com.agile.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.agile.admin.api.entity.SysDict;
import com.agile.admin.api.entity.SysDictItem;
import com.agile.admin.mapper.SysDictItemMapper;
import com.agile.admin.mapper.SysDictMapper;
import com.agile.admin.service.SysDictService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.enumeration.DictTypeEnum;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@AllArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictItemMapper dictItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R removeDictByIds(Long[] ids) {

        List<Long> dictIdList = baseMapper.selectBatchIds(CollUtil.toList(ids))
                .stream()
                // System built-in types are not deleted
                .filter(sysDict -> !sysDict.getSystemFlag().equals(DictTypeEnum.SYSTEM.getType()))
                .map(SysDict::getId)
                .collect(Collectors.toList());

        baseMapper.deleteBatchIds(dictIdList);

        dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery().in(SysDictItem::getDictId, dictIdList));
        return R.ok();
    }

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#dict.dictType")
    public R updateDict(SysDict dict) {
        SysDict sysDict = this.getById(dict.getId());
        // System built-in
        if (DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystemFlag())) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
        }
        this.updateById(dict);
        return R.ok(dict);
    }

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R syncDictCache() {
        return R.ok();
    }

}
