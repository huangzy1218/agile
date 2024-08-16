package com.agile.codegen.util;

import cn.hutool.core.util.StrUtil;
import com.agile.codegen.entity.GenDatasourceConf;
import com.agile.codegen.mapper.GenDatasourceConfMapper;
import com.agile.codegen.mapper.GeneratorMapper;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.datasource.enums.DsJdbcUrlEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Code generation utility class.
 *
 * @author Huang Z.Y.
 */
@UtilityClass
public class GenUtils {

    /**
     * Get function name from table name.
     *
     * @param tableName Table name.
     * @return Function name.
     */
    public String getFunctionName(String tableName) {
        return StrUtil.toCamelCase(tableName);
    }

    /**
     * Get module name from package name.
     *
     * @param packageName Package name.
     * @return Module name.
     */
    public String getModuleName(String packageName) {
        return StrUtil.subAfter(packageName, ".", true);
    }

    /**
     * Get mapper for the corresponding data source dialect.
     *
     * @param dsName Data source name.
     * @return GeneratorMapper.
     */
    public GeneratorMapper getMapper(String dsName) {
        // Get the target data source database type
        GenDatasourceConfMapper datasourceConfMapper = SpringContextHolder.getBean(GenDatasourceConfMapper.class);
        GenDatasourceConf datasourceConf = datasourceConfMapper
                .selectOne(Wrappers.<GenDatasourceConf>lambdaQuery().eq(GenDatasourceConf::getName, dsName));

        String dbConfType;
        // Default to MYSQL data source
        if (datasourceConf == null) {
            dbConfType = DsJdbcUrlEnum.MYSQL.getDbName();
        } else {
            dbConfType = datasourceConf.getDsType();
        }
        // Get all data implementations
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        Map<String, GeneratorMapper> beansOfType = context.getBeansOfType(GeneratorMapper.class);

        // Select mapper based on data type
        for (String key : beansOfType.keySet()) {
            if (StrUtil.containsIgnoreCase(key, dbConfType)) {
                return beansOfType.get(key);
            }
        }

        throw new IllegalArgumentException("Invalid dsName: " + dsName);
    }

}
