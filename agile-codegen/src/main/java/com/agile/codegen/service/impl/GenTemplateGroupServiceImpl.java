/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.agile.codegen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenTemplateGroupEntity;
import com.pig4cloud.pig.codegen.mapper.GenTemplateGroupMapper;
import com.pig4cloud.pig.codegen.service.GenTemplateGroupService;
import org.springframework.stereotype.Service;

/**
 * 模板分组关联表
 *
 * @author PIG
 * @date 2023-02-22 09:25:15
 */
@Service
public class GenTemplateGroupServiceImpl extends ServiceImpl<GenTemplateGroupMapper, GenTemplateGroupEntity>
        implements GenTemplateGroupService {

}
