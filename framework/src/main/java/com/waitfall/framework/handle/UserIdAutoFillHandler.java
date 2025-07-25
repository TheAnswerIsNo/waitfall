package com.waitfall.framework.handle;

import cn.dev33.satoken.stp.StpUtil;
import org.dromara.mpe.autofill.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 全局获取用户ID
 */
@Component
public class UserIdAutoFillHandler implements AutoFillHandler<String> {

    /**
     * @param object 当前操作的数据对象
     * @param clazz  当前操作的数据对象的class
     * @param field  当前操作的数据对象上的字段
     * @return userId
     */
    @Override
    public String getVal(Object object, Class<?> clazz, Field field) {
        return StpUtil.getLoginIdAsString();
    }
}