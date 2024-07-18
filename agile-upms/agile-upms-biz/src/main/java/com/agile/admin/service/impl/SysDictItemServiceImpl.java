package com.agile.admin.service.impl;

import com.agile.admin.api.entity.SysDict;
import com.agile.admin.api.entity.SysDictItem;
import com.agile.admin.mapper.SysDictItemMapper;
import com.agile.admin.service.SysDictItemService;
import com.agile.admin.service.SysDictService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.enumeration.DictTypeEnum;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;

/**
 * @author Huang Z.Y.
 */
@AllArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    private final SysDictService dictService;

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R removeDictItem(Long id) {
        // Query dictionary ID based on ID
        SysDictItem dictItem = this.getById(id);
        SysDict dict = dictService.getById(dictItem.getDictId());
        // System built-in
        if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_DELETE_SYSTEM));
        }
        return R.ok(this.removeById(id));
    }

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#item.dictType")
    public R updateDictItem(SysDictItem item) {
        // Query Dictionary
        SysDict dict = dictService.getById(item.getDictId());
        // System built-in
        if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
        }
        return R.ok(this.updateById(item));
    }

}
