package com.agile.common.mybatis.config;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.constant.CommonConstants;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * MybatisPlus autofill configuration.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * To fill in the value, first determine whether there is a manual setting, and give priority to the manually set value.
     * For example: the job must be set manually.
     *
     * @param fieldName  Attribute name
     * @param fieldVal   Attribute name
     * @param metaObject MetaObject
     * @param isCover    Whether to overwrite the original value to avoid manually entering parameters during update operations
     */
    private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
        // 0. If fill value is empty
        if (fieldVal == null) {
            return;
        }

        // 1. No set method
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. If the user has a manually set value
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
        if (StrUtil.isNotBlank(setValueStr) && !isCover) {
            return;
        }
        // 3. Set when field types are the same
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("Mybatis plus start insert fill.");
        LocalDateTime now = LocalDateTime.now();

        fillValIfNullByName("createTime", now, metaObject, true);
        fillValIfNullByName("updateTime", now, metaObject, true);
        fillValIfNullByName("createBy", getUserName(), metaObject, true);
        fillValIfNullByName("updateBy", getUserName(), metaObject, true);

        // Remove tag autocomplete
        fillValIfNullByName("delFlag", CommonConstants.STATUS_NORMAL, metaObject, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("Mybatis plus start update fill.");
        fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
        fillValIfNullByName("updateBy", getUserName(), metaObject, true);
    }

    /**
     * Get the current username of Spring Security.
     *
     * @return Current username
     */
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Anonymous interface returns directly
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        if (Optional.ofNullable(authentication).isPresent()) {
            return authentication.getName();
        }
        return null;
    }

}
