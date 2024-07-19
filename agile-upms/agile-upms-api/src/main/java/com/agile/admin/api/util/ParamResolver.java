package com.agile.admin.api.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.feign.RemoteParamService;
import com.agile.common.core.util.SpringContextHolder;
import lombok.experimental.UtilityClass;

/**
 * System parameter configuration parser.
 *
 * @author Huang Z.Y.
 */
@UtilityClass
public class ParamResolver {

    /**
     * Query value configuration based on key.
     *
     * @param key        Key
     * @param defaultVal Default value
     * @return Long value
     */
    public Long getLong(String key, Long... defaultVal) {
        return checkAndGet(key, Long.class, defaultVal);
    }

    /**
     * Query value configuration based on key.
     *
     * @param key        Key
     * @param defaultVal Default value
     * @return String value
     */
    public String getStr(String key, String... defaultVal) {
        return checkAndGet(key, String.class, defaultVal);
    }

    private <T> T checkAndGet(String key, Class<T> clazz, T... defaultVal) {
        // Verify whether the input parameters are legal
        if (StrUtil.isBlank(key) || defaultVal.length > 1) {
            throw new IllegalArgumentException("参数不合法");
        }

        RemoteParamService remoteParamService = SpringContextHolder.getBean(RemoteParamService.class);

        String result = remoteParamService.getByKey(key).getData();

        if (StrUtil.isNotBlank(result)) {
            return Convert.convert(clazz, result);
        }

        if (defaultVal.length == 1) {
            return Convert.convert(clazz, defaultVal.clone()[0]);

        }
        return null;
    }

}

