package com.agile.codegen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Dynamic query.
 *
 * @author lengleng
 * @date 2022-07-09
 */
@Mapper
public interface GenDynamicMapper {

    @InterceptorIgnore(tenantLine = "true")
    List<LinkedHashMap<String, Object>> dynamicQuerySql(@Param("value") String sql);

}
